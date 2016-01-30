package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.cloudbees.util.rhino.sandbox.SandboxContextFactory;
import com.cloudbees.util.rhino.sandbox.SandboxNativeJavaObject;

import de.oglimmer.cyc.IDataBackend;
import de.oglimmer.cyc.api.Constants.Mode;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.util.ExceptionConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Game {

	static {
		ContextFactory.initGlobal(new SandboxContextFactory());
	}

	private IDataBackend dataProvider;

	@Getter(AccessLevel.PACKAGE)
	private Grocer grocer = new Grocer(this);

	@Getter(AccessLevel.PACKAGE)
	private Collection<Company> companies = new ArrayList<>();

	@Getter(AccessLevel.PACKAGE)
	private GameRun gameRun = new GameRun();

	@Getter(AccessLevel.PACKAGE)
	private List<City> cities = new ArrayList<>();

	@Getter
	private final int totalYear;
	@Getter
	private final int totalMonth;
	@Getter
	private final int totalDay;
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private int currentDay;

	@Getter(AccessLevel.PACKAGE)
	private Constants constants;

	@Getter(AccessLevel.PACKAGE)
	private Collection<Cache<?>> caches = new HashSet<>();

	@Getter(AccessLevel.PACKAGE)
	private DailyStatisticsManager dailyStatisticsManager = new DailyStatisticsManager();

	public Game(Mode mode, IDataBackend dataProvider) {
		this.dataProvider = dataProvider;
		this.constants = new Constants(mode);
		this.totalYear = constants.getRuntimeYear();
		this.totalMonth = constants.getRuntimeMonth();
		this.totalDay = constants.getRuntimeDay();
	}

	public GameRun executeGame(List<User> userList) {

		gameRun.setStartTime(new Date());

		readScripts(userList);

		createCities();

		allYears();

		calcResults();

		if (constants.getMode() == Mode.FULL) {
			dataProvider.writeGameResults(gameRun);
		}

		log.debug("Run completed in {} millies.", gameRun.getEndTime().getTime() - gameRun.getStartTime().getTime());

		return gameRun;
	}

	void createCities() {
		for (int i = cities.size(); i < constants
				.getNumberCities(Math.max(companies.size(), getNumberTotalEstablishments())); i++) {
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

			gameRun.getResult().getCreateNotExists(company.getName()).setTotalAssets(company.getTotalAssets());
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
				Object jsSystemout = new SandboxNativeJavaObject(scope,
						new DebugAdapter(this, gameRun.getResult(), company.getName()), DebugAdapter.class);
				prototype.put("out", scope, jsSystemout);
				prototype.put("console", scope, jsSystemout);

				try {
					long time = System.nanoTime();
					context.evaluateString(scope, user.getMainJavaScript(), company.getName(), 1, null);
					getResult().getCreateNotExists(company.getName()).addRunTime("init", System.nanoTime() - time);
					getResult().getCreateNotExists(company.getName()).setCode(user.getMainJavaScript());
				} catch (RhinoException e) {
					if (e.getCause() instanceof GameException) {
						log.info("Failed to initialize the JavaScript, but found a GameException", e);
					} else {
						String formattedStackTrace = ExceptionConverter.convertToString(e);
						gameRun.getResult().addError(formattedStackTrace);
						log.error("Failed to initialize the JavaScript. Player " + company.getName() + " bankrupt", e);
						company.setBankruptFromError(formattedStackTrace);
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
