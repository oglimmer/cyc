package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrappedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudbees.util.rhino.sandbox.SandboxContextFactory;
import com.cloudbees.util.rhino.sandbox.SandboxNativeJavaObject;

import de.oglimmer.cyc.api.Grocer.FoodOrder;
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

	public GameResult getResult() {
		return result;
	}

	public GameRun executeGame(List<String[]> userList, boolean writeGameResult) {

		gameRun.setStartTime(new Date());

		RealEstateProfiles.readCities(cities, userList.size());

		readScripts(userList, writeGameResult);

		processGame();

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

	private void processGame() {
		for (int year = 1; year <= totalYear; year++) {
			processYear(year);
		}
		result.setTotalDays(currentDay);
	}

	public int getCurrentDay() {
		return currentDay;
	}

	private void processYear(int year) {

		callLaunch();
		currentDay = 0;

		for (int month = 1; month <= totalMonth; month++) {
			log.debug("Year: {}", year);
			processMonth(month);
		}

		if (year == 1) {
			payCredits();
		}
	}

	private void callLaunch() {
		for (Company company : companies) {
			if (!company.isBankrupt()) {
				try {
					if (company.launch != null) {
						ThreadLocal.setCompany(company);
						company.launch.run();
					}
				} catch (WrappedException e) {
					if (!(e.getCause() instanceof GameException)) {
						result.addError(e);
						log.error("Failed to call the company.launch handler", e);
					}
				} catch (EcmaError e) {
					result.addError(e);
					log.error("Failed to call the company.launch handler. Player " + company.getName() + " bankrupt", e);
					company.setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}

	private void payCredits() {
		for (Company company : companies) {
			if (!company.isBankrupt()) {
				try {
					log.debug("Company {} paid the initial bank credit $55,000", company.getName());
					company.decCash(55_000);
				} catch (OutOfMoneyException e) {
					log.debug("Company [] is bankrupt", e.getCompany());
				}
			}
		}
	}

	private void processMonth(int month) {
		log.debug("Month: {}", month);

		processRealEstateBusiness();
		processHumanResources();
		payRents();

		callMonthly();

		for (int day = 1; day <= totalDay; day++) {
			processDay(day);
		}

		paySalaries();

	}

	private void callMonthly() {
		for (Company company : companies) {
			if (!company.isBankrupt()) {
				try {
					if (company.doMonthly != null) {
						ThreadLocal.setCompany(company);
						company.doMonthly.run();
					}
				} catch (WrappedException e) {
					if (!(e.getCause() instanceof GameException)) {
						result.addError(e);
						log.error("Failed to call the company.doMonthly handler", e);
					}
				} catch (EcmaError e) {
					result.addError(e);
					log.error(
							"Failed to call the company.doMonthly handler. Player " + company.getName() + " bankrupt",
							e);
					company.setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}

	private void processDay(int day) {
		currentDay++;
		log.debug("Day: {}/{}", day, currentDay);

		for (Company c : companies) {
			result.get(c.getName()).addEstablishmentsByDays(c.getEstablishments().size());
		}

		grocer.initDay();

		if ((day + 1) % 7 == 0) {
			callWeekly();
		}

		callDaily();

		deliverFood();

		runBusiness();

		cleanFoodStorages();

	}

	private void callWeekly() {
		for (Company company : companies) {
			if (!company.isBankrupt()) {
				try {
					if (company.doWeekly != null) {
						ThreadLocal.setCompany(company);
						company.doWeekly.run();
					}
				} catch (WrappedException e) {
					if (!(e.getCause() instanceof GameException)) {
						result.addError(e);
						log.error("Failed to call the company.doWeekly handler", e);
					}
				} catch (EcmaError e) {
					result.addError(e);
					log.error("Failed to call the company.doWeekly handler. Player " + company.getName() + " bankrupt",
							e);
					company.setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}

	private void callDaily() {
		for (Company company : companies) {
			if (!company.isBankrupt()) {
				try {
					if (company.doDaily != null) {
						ThreadLocal.setCompany(company);
						company.doDaily.run();
					}
				} catch (WrappedException e) {
					if (!(e.getCause() instanceof GameException)) {
						result.addError(e);
						log.error("Failed to call the company.doDaily handler", e);
					}
				} catch (EcmaError e) {
					result.addError(e);
					log.error("Failed to call the company.doDaily handler. Player " + company.getName() + " bankrupt",
							e);
					company.setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}

	private void cleanFoodStorages() {
		for (Company c : companies) {
			for (Establishment est : c.getEstablishmentsInt()) {
				for (Iterator<FoodUnit> it = est.getStoredFoodUnitsInt().iterator(); it.hasNext();) {
					FoodUnit fu = it.next();
					if (fu.getUnits() == 0) {
						it.remove();
						// log.debug("Removed an empty food-unit of {} for {} in {}", fu.getFood(),
						// c.getName(), est.getAddress());
					} else {
						fu.decPullDate();
						if (fu.getPullDate() == 0) {
							it.remove();
							log.debug("Removed a rotten food-unit of {} for {} with {} in {}", fu.getFood(),
									c.getName(), fu.getUnits(), est.getAddress());
							result.get(c.getName()).getTotalRottenFood().add(fu.getFood().toString(), fu.getUnits());
						}
					}
				}
			}
		}
	}

	private void deliverFood() {
		Map<Company, FoodDelivery> map = new HashMap<Company, FoodDelivery>();
		for (Iterator<FoodOrder> it = grocer.getFoodOrders().iterator(); it.hasNext();) {
			FoodOrder fo = it.next();
			if (fo.getDays() > 1) {
				fo.decDays();
			} else {
				it.remove();
			}
			FoodUnit fu = new FoodUnit(fo.getFood(), fo.getUnits());
			FoodDelivery list = map.get(fo.getCompany());
			if (list == null) {
				list = new FoodDelivery();
				map.put(fo.getCompany(), list);
			}
			list.add(fu);
		}
		for (Company c : map.keySet()) {
			FoodDelivery fd = map.get(c);
			log.debug("Food delivery for {} = {}", c.getName(), fd);
			try {
				if (c.foodDelivery != null) {
					ThreadLocal.setCompany(c);
					c.foodDelivery.run(fd);
				}
			} catch (WrappedException e) {
				if (!(e.getCause() instanceof GameException)) {
					result.addError(e);
					log.error("Failed to call the company.foodDelivery handler", e);
				}
			} catch (EcmaError e) {
				result.addError(e);
				log.error("Failed to call the company.foodDelivery handler. Player " + c.getName() + " bankrupt", e);
				c.setBankruptFromError(e);
			}
		}
		ThreadLocal.resetCompany();
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
				Object jsSystemout = new SandboxNativeJavaObject(scope, new DebugAdapter(result, writeGameResult), DebugAdapter.class);
				prototype.put("out", scope, jsSystemout);

				try {
					context.evaluateString(scope, user[1], company.getName(), 1, null);
				} catch (WrappedException e) {
					if (!(e.getCause() instanceof GameException)) {
						result.addError(e);
						log.error("Failed to initialize the JavaScript", e);
					}
				} catch (EcmaError e) {
					result.addError(e);
					log.error("Failed to initialize the JavaScript. Player " + company.getName() + " bankrupt", e);
					company.setBankruptFromError(e);
				}

				companies.add(company);
			}
			ThreadLocal.resetCompany();
		} finally {
			Context.exit();
		}
	}

	public void processRealEstateBusiness() {
		RealEstateProfiles ap = new RealEstateProfiles(cities, companies.size());
		boolean pickedOne = true;
		while (ap.iterator().hasNext() && pickedOne) {
			pickedOne = false;

			for (Company c : companies) {
				if (!c.isBankrupt()) {
					ThreadLocal.setCompany(c);
					if (c.realEstateAgent != null) {
						try {
							c.realEstateAgent.run(ap);
						} catch (WrappedException e) {
							if (!(e.getCause() instanceof GameException)) {
								result.addError(e);
								log.error("Failed to call the company.realEstateAgent handler", e);
							}
						} catch (EcmaError e) {
							result.addError(e);
							log.error("Failed to call the company.realEstateAgent handler. Player " + c.getName()
									+ " bankrupt", e);
							c.setBankruptFromError(e);
						}
					}
				}
			}
			ThreadLocal.resetCompany();

			for (Iterator<RealEstateProfile> it = ap.iteratorInt(); it.hasNext();) {
				RealEstateProfile p = it.next();
				try {
					Map<String, Object> en = ap.getOfferFor(p);
					if (en != null) {
						it.remove();
						pickedOne = true;
						Integer bribe = (Integer) en.get("bribe");
						Boolean buy = (Boolean) en.get("buy");
						Company company = (Company) en.get("company");
						company.decCash(bribe);
						Establishment est = new Establishment(company, p.getCity(), p.getLocationQuality(),
								p.getLocationSize(), p.getLeaseCost(), p.getSalePrice());
						company.getEstablishmentsInt().add(est);
						if (buy) {
							log.debug("{} bought {} for ${}", company.getName(), p, p.getSalePrice());
							est.buy();
						} else {
							log.debug("{} rented {} for ${}", company.getName(), p, p.getLeaseCost());
						}
					}
				} catch (OutOfMoneyException e) {
					log.debug("Company {} is bankrupt", e.getCompany());
				}
			}
		}
	}

	public void paySalaries() {
		for (Company c : companies) {
			try {
				if (!c.isBankrupt()) {
					for (Employee e : c.getHumanResources().getEmployeesInt()) {
						c.decCash(e.getSalary());
						result.get(c.getName()).addTotalNumSalariesPaid(e.getJobPosition().toString(), e.getSalary(),
								totalDay);
						log.debug("{} payed ${} for {}", c.getName(), e.getSalary(), e.getName());
					}
				}
			} catch (OutOfMoneyException e) {
				log.debug("Company {} is bankrupt", e.getCompany());
			}
		}
	}

	public void payRents() {
		for (Company c : companies) {
			try {
				if (!c.isBankrupt()) {
					for (Establishment e : c.getEstablishmentsInt()) {
						if (e.isRented()) {
							c.decCash(e.getLeaseCost());
							result.get(c.getName()).addTotalOnRent(e.getLeaseCost());
							log.debug("{} payed ${} for {}", c.getName(), e.getLeaseCost(), e.getAddress());
						}
					}
				}
			} catch (OutOfMoneyException e) {
				log.debug("Company {} is bankrupt", e.getCompany());
			}
		}
	}

	public void processHumanResources() {

		ApplicationProfiles ap = new ApplicationProfiles(companies.size());

		boolean pickedOne = true;
		while (ap.iterator().hasNext() && pickedOne) {
			pickedOne = false;

			for (Company c : companies) {
				if (!c.isBankrupt()) {
					ThreadLocal.setCompany(c);
					if (c.getHumanResources().hiringProcess != null) {
						try {
							c.getHumanResources().hiringProcess.run(ap);
						} catch (WrappedException e) {
							if (!(e.getCause() instanceof GameException)) {
								result.addError(e);
								log.error("Failed to call the company.hiringProcess handler", e);
							}
						} catch (EcmaError e) {
							result.addError(e);
							log.error("Failed to call the company.hiringProcess handler. Player " + c.getName()
									+ " bankrupt", e);
							c.setBankruptFromError(e);
						}
					}
				}
			}
			ThreadLocal.resetCompany();

			for (Iterator<ApplicationProfile> it = ap.iteratorInt(); it.hasNext();) {
				ApplicationProfile p = it.next();
				Entry<Company, Object[]> en = ap.getMaxOfferFor(p);
				if (en != null) {
					pickedOne = true;
					it.remove();
					Establishment est = (Establishment) en.getValue()[0];
					Integer offer = (Integer) en.getValue()[1];
					Company company = en.getKey();
					if (offer >= p.getDesiredSalary()) {
						hire(p, offer, est, company);
					} else {
						double rnd = Math.random() * p.getDesiredSalary();
						if (rnd < offer) {
							hire(p, offer, est, company);
						}
					}
				}
			}
		}

	}

	private void hire(ApplicationProfile p, int salary, Establishment est, Company company) {
		log.debug("{} hired {} for ${}", company.getName(), p, salary);
		company.getHumanResources().getEmployeesInt()
				.add(new Employee(p.getName(), est, p.getQualification(), p.getJobPosition(), salary));
	}

	public void runBusiness() {
		for (String city : cities) {
			int totalGuests = (int) (Math.random() * companies.size() * 100) + 50;
			log.debug("Guests for today in {}: {}", city, totalGuests);
			result.getGuestsTotalPerCity().add(city, totalGuests);

			int totalScore = 0;
			Map<Integer, Establishment> estList = new LinkedHashMap<>();
			for (Company c : companies) {
				for (Establishment est : c.getEstablishmentsInt()) {
					if (est.getAddress().startsWith(city)) {
						int score = est.getScore();
						if (score > 0) {
							estList.put(totalScore + score, est);
							totalScore += score;
						}
					}
				}
			}

			if (estList.isEmpty()) {
				log.debug("No suiteable restaurants in {}", city);
			} else {
				while (totalGuests-- > 0) {
					Guest guest = new Guest(result);
					log.debug("A guest thinks about food in {}", city);
					double rnd = Math.random() * totalScore;
					for (Integer i : estList.keySet()) {
						assert rnd != -1;
						if (rnd <= i) {
							Establishment est = estList.get(i);
							log.debug("A guest decided for {} by {}", est.getAddress(), est.getParent().getName());
							guest.serveGuest(est.getParent(), est, city);
							rnd = -1;
							break;
						}
					}
					assert rnd == -1;
				}
			}
		}
	}

}
