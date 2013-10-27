package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.WrappedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.cyc.api.Grocer.FoodOrder;

public class Day {

	private Logger log = LoggerFactory.getLogger(Day.class);

	private Game game;
	private OpeningHours openingHours;

	public Day(Game game) {
		this.game = game;
		this.openingHours = new OpeningHours(game);
	}

	void processDay(int day) {

		incDailyCounter(day);

		game.getGrocer().initDay();

		if ((day + 1) % 7 == 0) {
			callWeekly();
		}

		callDaily();

		deliverFood();

		openingHours.runBusiness();

		cleanFoodStorages();

	}

	private void incDailyCounter(int day) {
		game.setCurrentDay(game.getCurrentDay() + 1);
		log.debug("Day: {}/{}", day, game.getCurrentDay());

		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				PlayerResult playerResult = game.getResult().get(c.getName());
				playerResult.addEstablishmentsByDays(c.getEstablishments().size());
				for (Employee e : c.getHumanResources().getEmployeesInt()) {
					playerResult.addStaffByDays(e.getJobPosition().toString());
				}
				log.debug("{} at day {} => est={}, staff={} ", c.getName(), game.getCurrentDay(),
						game.getResult().get(c.getName()).getEstablishmentsByDays(), game.getResult().get(c.getName())
								.getStaffByDays());
			} else {
				log.debug("{} is bankrupt...", c.getName());
			}
		}
	}

	private void callWeekly() {
		for (Company company : game.getCompanies()) {
			if (!company.isBankrupt()) {
				try {
					if (company.doWeekly != null) {
						ThreadLocal.setCompany(company);
						company.doWeekly.run();
					}
				} catch (WrappedException e) {
					if (!(e.getCause() instanceof GameException)) {
						game.getResult().addError(e);
						log.error("Failed to call the company.doWeekly handler", e);
					}
				} catch (EcmaError e) {
					game.getResult().addError(e);
					log.error("Failed to call the company.doWeekly handler. Player " + company.getName() + " bankrupt",
							e);
					company.setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}

	private void callDaily() {
		for (Company company : game.getCompanies()) {
			if (!company.isBankrupt()) {
				try {
					if (company.doDaily != null) {
						ThreadLocal.setCompany(company);
						company.doDaily.run();
					}
				} catch (WrappedException e) {
					if (!(e.getCause() instanceof GameException)) {
						game.getResult().addError(e);
						log.error("Failed to call the company.doDaily handler", e);
					}
				} catch (EcmaError e) {
					game.getResult().addError(e);
					log.error("Failed to call the company.doDaily handler. Player " + company.getName() + " bankrupt",
							e);
					company.setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}

	private void cleanFoodStorages() {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
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
								game.getResult().get(c.getName()).getTotalRottenFood()
										.add(fu.getFood().toString(), fu.getUnits());
							}
						}
					}
				}
			}
		}
	}

	private void deliverFood() {
		Map<Company, FoodDelivery> map = new HashMap<Company, FoodDelivery>();
		for (Iterator<FoodOrder> it = game.getGrocer().getFoodOrders().iterator(); it.hasNext();) {
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
			if (!c.isBankrupt()) {
				FoodDelivery fd = map.get(c);
				log.debug("Food delivery for {} = {}", c.getName(), fd);
				try {
					if (c.foodDelivery != null) {
						ThreadLocal.setCompany(c);
						c.foodDelivery.run(fd);
					}
				} catch (WrappedException e) {
					if (!(e.getCause() instanceof GameException)) {
						game.getResult().addError(e);
						log.error("Failed to call the company.foodDelivery handler", e);
					}
				} catch (EcmaError e) {
					game.getResult().addError(e);
					log.error("Failed to call the company.foodDelivery handler. Player " + c.getName() + " bankrupt", e);
					c.setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}
}
