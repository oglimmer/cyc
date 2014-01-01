package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
import de.oglimmer.cyc.dao.GameWinnersDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.dao.couchdb.GameWinnersCouchDb;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.GameWinners;
import de.oglimmer.cyc.model.GameWinners.GameWinnerEntry;
import de.oglimmer.cyc.model.User;

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
	private GameRun gameRun = new GameRun();

	@Getter(AccessLevel.PACKAGE)
	private List<City> cities = new ArrayList<>();

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

	@Getter(AccessLevel.PACKAGE)
	private Collection<Cache<?>> caches = new HashSet<>();

	public Game(Mode mode) {
		constants = new Constants(mode);
		this.totalYear = constants.getRuntimeYear();
		this.totalMonth = constants.getRuntimeMonth();
		this.totalDay = constants.getRuntimeDay();
	}

	public GameRun executeGame(List<User> userList, boolean writeGameResult) {

		gameRun.setStartTime(new Date());

		readScripts(userList);

		createCities();

		allYears();

		calcResults();

		if (writeGameResult) {
			writeGameResults();
		}

		log.debug("Run completed in {} millies.", gameRun.getEndTime().getTime() - gameRun.getStartTime().getTime());

		return gameRun;
	}

	private void writeGameResults() {
		GameRunDao gameRunDao = new GameRunCouchDb(CouchDbUtil.getDatabase());
		gameRunDao.add(gameRun);

		GameWinnersDao gameWinnersDao = new GameWinnersCouchDb(CouchDbUtil.getDatabase());
		GameWinners gw = new GameWinners();
		gw.setRefGameRunId(gameRun.getId());
		gw.setStartTime(gameRun.getStartTime());
		for (String company : gameRun.getParticipants()) {
			gw.getParticipants().add(new GameWinnerEntry(company, gameRun.getResult().get(company).getTotalAssets()));
		}
		gameWinnersDao.add(gw);
	}

	void createCities() {
		for (int i = cities.size(); i < constants.getNumberCities(Math.max(companies.size(),
				getNumberTotalEstablishments())); i++) {
			String city = constants.getCity(cities);
			cities.add(new City(city, this));
			log.debug("Created city={}", city);
		}
	}

	private int getNumberTotalEstablishments() {
		int totalEst = 0;
		for (Company company : companies) {
			totalEst += company.getEstablishmentsInt().size();
		}
		return totalEst;
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

			gameRun.getResult().get(company.getName()).setTotalAssets(company.getTotalAssets());
		}

		gameRun.setEndTime(new Date());
	}

	private void allYears() {
		currentDay = 0;
		Year year = new Year(this);
		for (int yearCount = 1; yearCount <= totalYear; yearCount++) {
			year.processYear(yearCount);
		}
		gameRun.getResult().setTotalDays(currentDay);
		year.close();
	}

	private void readScripts(List<User> userList) {

		ContextFactory contextFactory = ContextFactory.getGlobal();
		Context context = contextFactory.enterContext();
		try {

			for (User user : userList) {
				Company company = new Company(this, user.getUsername(), grocer);
				ThreadLocal.setCompany(company);

				ScriptableObject prototype = context.initStandardObjects();
				prototype.setParentScope(null);
				Scriptable scope = context.newObject(prototype);
				scope.setPrototype(prototype);

				Object jsCompany = new SandboxNativeJavaObject(scope, company, Company.class);
				prototype.put("company", scope, jsCompany);
				Object jsSystemout = new SandboxNativeJavaObject(scope, new DebugAdapter(gameRun.getResult(),
						company.getName()), DebugAdapter.class);
				prototype.put("out", scope, jsSystemout);
				prototype.put("console", scope, jsSystemout);

				try {
					context.evaluateString(scope, user.getMainJavaScript(), company.getName(), 1, null);
				} catch (RhinoException e) {
					if (e.getCause() instanceof GameException) {
						log.info("Failed to initialize the JavaScript, but found a GameException", e);
					} else {
						gameRun.getResult().addError(e);
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

	GameResult getResult() {
		return gameRun.getResult();
	}

}
