package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.cloudbees.util.rhino.sandbox.SandboxContextFactory;
import com.cloudbees.util.rhino.sandbox.SandboxNativeJavaObject;

import de.oglimmer.cyc.api.Constants.Mode;
import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.model.GameRun;

@Slf4j
public class Game {

	static {
		ContextFactory.initGlobal(new SandboxContextFactory());
	}

	@Getter(AccessLevel.PACKAGE)
	private Grocer grocer = new Grocer(this);

	@Getter(AccessLevel.PACKAGE)
	private Collection<Company> companies = new ArrayList<>();

	@Getter(AccessLevel.PACKAGE)
	private GameResult result = new GameResult();
	@Getter(AccessLevel.PACKAGE)
	private GameRun gameRun = new GameRun();

	@Getter(AccessLevel.PACKAGE)
	private List<String> cities = new ArrayList<>();

	@Getter(AccessLevel.PACKAGE)
	private final int totalYear;
	@Getter(AccessLevel.PACKAGE)
	private final int totalMonth;
	@Getter(AccessLevel.PACKAGE)
	private final int totalDay;
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private int currentDay;

	@Getter(AccessLevel.PACKAGE)
	private Constants constants;

	public Game(Mode mode) {
		constants = new Constants(mode);
		this.totalYear = constants.getRuntimeYear();
		this.totalMonth = constants.getRuntimeMonth();
		this.totalDay = constants.getRuntimeDay();
	}

	public GameRun executeGame(List<String[]> userList, boolean writeGameResult) {

		gameRun.setStartTime(new Date());

		RealEstateProfiles.readCities(this, cities, userList.size());

		readScripts(userList);

		allYears();

		calcResults();

		if (writeGameResult) {
			GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());
			dao.add(gameRun);
		}

		log.debug("Run completed in {} millies.", gameRun.getEndTime().getTime() - gameRun.getStartTime().getTime());

		return gameRun;
	}

	private void calcResults() {
		for (Company c : companies) {
			if (c.isBankrupt()) {
				log.debug("{} became bankrupt.", c.getName());
			} else {
				log.debug("{} got ${}", c.getName(), c.getCash());
			}
		}

		for (Company company : companies) {
			gameRun.getParticipants().add(company.getName());

			result.get(company.getName()).setTotalAssets(company.getTotalAssets());
		}

		gameRun.setResult(result);
		gameRun.setEndTime(new Date());
	}

	private void allYears() {
		currentDay = 0;
		Year year = new Year(this);
		for (int yearCount = 1; yearCount <= totalYear; yearCount++) {
			year.processYear(yearCount);
		}
		result.setTotalDays(currentDay);
		year.close();
	}

	private void readScripts(List<String[]> userList) {

		ContextFactory contextFactory = ContextFactory.getGlobal();
		Context context = contextFactory.enterContext();
		try {

			for (String[] user : userList) {
				Company company = new Company(this, user[0], grocer);
				ThreadLocal.setCompany(company);

				ScriptableObject prototype = context.initStandardObjects();
				prototype.setParentScope(null);
				Scriptable scope = context.newObject(prototype);
				scope.setPrototype(prototype);

				Object jsCompany = new SandboxNativeJavaObject(scope, company, Company.class);
				prototype.put("company", scope, jsCompany);
				Object jsSystemout = new SandboxNativeJavaObject(scope, new DebugAdapter(result, company.getName()),
						DebugAdapter.class);
				prototype.put("out", scope, jsSystemout);
				prototype.put("console", scope, jsSystemout);

				try {
					context.evaluateString(scope, user[1], company.getName(), 1, null);
				} catch (RhinoException e) {
					if (e.getCause() instanceof GameException) {
						log.info("Failed to initialize the JavaScript, but found a GameException", e);
					} else {
						result.addError(e);
						log.error("Failed to initialize the JavaScript. Player " + company.getName() + " bankrupt", e);
						company.setBankruptFromError(e);
					}
				}

				companies.add(company);
			}
			ThreadLocal.resetCompany();
		} finally {
			Context.exit();
		}
	}

}
