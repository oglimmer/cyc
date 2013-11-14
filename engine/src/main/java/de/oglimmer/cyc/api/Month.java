package de.oglimmer.cyc.api;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.WrappedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Month {

	private Logger log = LoggerFactory.getLogger(Month.class);

	private Game game;
	private Day day;

	public Month(Game game) {
		this.game = game;
		this.day = new Day(game);
	}

	void processMonth(int month) {
		log.debug("Month: {}", month);

		processRealEstateBusiness();
		processHumanResources();

		payRents();

		callMonthly();

		for (int dayCount = 1; dayCount <= game.getTotalDay(); dayCount++) {
			day.processDay(dayCount);
		}

		paySalaries();

	}

	private void callMonthly() {
		for (Company company : game.getCompanies()) {
			if (!company.isBankrupt()) {
				try {
					if (company.doMonthly != null) {
						ThreadLocal.setCompany(company);
						company.doMonthly.run();
					}
				} catch (WrappedException e) {
					if (!(e.getCause() instanceof GameException)) {
						game.getResult().addError(e);
						log.error("Failed to call the company.doMonthly handler", e);
					}
				} catch (EcmaError e) {
					game.getResult().addError(e);
					log.error(
							"Failed to call the company.doMonthly handler. Player " + company.getName() + " bankrupt",
							e);
					company.setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}

	public void paySalaries() {
		for (Company c : game.getCompanies()) {
			try {
				if (!c.isBankrupt()) {
					for (Employee e : c.getHumanResources().getEmployeesInt()) {
						game.getResult().get(c.getName())
								.addTotalOnSalaries(e.getJobPosition().toString(), e.getSalary());
						c.decCash(e.getSalary());
						log.debug("{} payed ${} for {}", c.getName(), e.getSalary(), e.getName());
					}
				}
			} catch (OutOfMoneyException e) {
				log.debug("Company {} is bankrupt", e.getCompany());
			}
		}
	}

	public void payRents() {
		for (Company c : game.getCompanies()) {
			try {
				if (!c.isBankrupt()) {
					for (Establishment e : c.getEstablishmentsInt()) {
						if (e.isRented()) {
							game.getResult().get(c.getName()).addTotalOnRent(e.getLeaseCost());
							c.decCash(e.getLeaseCost());
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

		ApplicationProfiles ap = new ApplicationProfiles(game.getCompanies().size());
		log.debug(ap.toString());

		boolean pickedOne = true;
		while (ap.iterator().hasNext() && pickedOne) {
			log.debug("round...");
			pickedOne = false;

			for (Company c : game.getCompanies()) {
				if (!c.isBankrupt()) {
					ThreadLocal.setCompany(c);
					if (c.getHumanResources().hiringProcess != null) {
						try {
							c.getHumanResources().hiringProcess.run(ap);
						} catch (WrappedException e) {
							if (!(e.getCause() instanceof GameException)) {
								game.getResult().addError(e);
								log.error("Failed to call the company.hiringProcess handler", e);
							}
						} catch (EcmaError e) {
							game.getResult().addError(e);
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

	public void processRealEstateBusiness() {
		RealEstateProfiles ap = new RealEstateProfiles(game.getCities(), game.getCompanies().size());
		log.debug(ap.toString());
		boolean pickedOne = true;
		while (ap.iterator().hasNext() && pickedOne) {
			pickedOne = false;

			for (Company c : game.getCompanies()) {
				if (!c.isBankrupt()) {
					ThreadLocal.setCompany(c);
					if (c.realEstateAgent != null) {
						try {
							c.realEstateAgent.run(ap);
						} catch (WrappedException e) {
							if (!(e.getCause() instanceof GameException)) {
								game.getResult().addError(e);
								log.error("Failed to call the company.realEstateAgent handler", e);
							}
						} catch (EcmaError e) {
							game.getResult().addError(e);
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
						game.getResult().get(company.getName()).addTotalBribe(bribe);
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

}
