package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudbees.util.rhino.sandbox.SandboxContextFactory;
import com.cloudbees.util.rhino.sandbox.SandboxNativeJavaObject;

import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.model.GameRun;

public class Game {

	static {
		ContextFactory.initGlobal(new SandboxContextFactory());
	}

	private Logger log = LoggerFactory.getLogger(Game.class);

	private Grocer grocer = new Grocer(this);

	private Collection<Company> companies = new ArrayList<>();

	private GameResult result = new GameResult();
	private GameRun gameRun = new GameRun();

	private List<String> cities = new ArrayList<>();

	private final int totalYear;
	private final int totalMonth;
	private final int totalDay;
	private int currentDay;

	public Game(int totalYear, int totalMonth, int totalDay) {
		this.totalYear = totalYear;
		this.totalMonth = totalMonth;
		this.totalDay = totalDay;
	}

	GameResult getResult() {
		return result;
	}

	Collection<Company> getCompanies() {
		return companies;
	}

	Grocer getGrocer() {
		return grocer;
	}

	GameRun getGameRun() {
		return gameRun;
	}

	List<String> getCities() {
		return cities;
	}

	int getTotalYear() {
		return totalYear;
	}

	int getTotalMonth() {
		return totalMonth;
	}

	int getTotalDay() {
		return totalDay;
	}

	void setCurrentDay(int currentDay) {
		this.currentDay = currentDay;
	}

	int getCurrentDay() {
		return currentDay;
	}

	public GameRun executeGame(List<String[]> userList, boolean writeGameResult) {

		gameRun.setStartTime(new Date());

		RealEstateProfiles.readCities(cities, userList.size());

		readScripts(userList, writeGameResult);

		allYears();

		printResults();

		gameRun.setEndTime(new Date());
		for (Company company : companies) {
			gameRun.getParticipants().add(company.getName());

			int totalAssets = company.getCash();
			for (Establishment est : company.getEstablishments()) {
				if (!est.isRented()) {
					totalAssets += est.getSalePrice();
				}
			}
			result.get(company.getName()).setTotalAssets(totalAssets);
		}

		gameRun.setResult(result);
		gameRun.setEndTime(new Date());
		gameRun.setMemUsed(Runtime.getRuntime().totalMemory());

		if (writeGameResult) {
			GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());
			dao.add(gameRun);
		}

		log.debug("Run completed in {} millies.", gameRun.getEndTime().getTime() - gameRun.getStartTime().getTime());

		return gameRun;
	}

	private void printResults() {
		for (Company c : companies) {
			if (c.isBankrupt()) {
				log.debug("{} became bankrupt.", c.getName());
			} else {
				log.debug("{} got ${}", c.getName(), c.getCash());
			}
		}
	}

	private void allYears() {
		currentDay = 0;
		Year year = new Year(this);
		for (int yearCount = 1; yearCount <= totalYear; yearCount++) {
			year.processYear(yearCount);
		}
		result.setTotalDays(currentDay);
	}

	private void readScripts(List<String[]> userList, boolean writeGameResult) {

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
