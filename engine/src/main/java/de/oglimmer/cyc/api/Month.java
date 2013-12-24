package de.oglimmer.cyc.api;

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.mozilla.javascript.RhinoException;

import de.oglimmer.cyc.api.ApplicationProfile.CompanyOffer;

@Slf4j
public class Month {

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
				callMonthlyCompany(company);
			}
		}
		ThreadLocal.resetCompany();
	}

	private void callMonthlyCompany(Company company) {
		try {
			if (company.doMonthly != null) {
				ThreadLocal.setCompany(company);
				company.doMonthly.run();
			}
		} catch (RhinoException e) {
			if (!(e.getCause() instanceof GameException)) {
				game.getResult().addError(e);
				log.error("Failed to call the company.doMonthly handler. Player " + company.getName() + " bankrupt", e);
				company.setBankruptFromError(e);
			}
		}
	}

	public void paySalaries() {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				c.paySalaries();
			}
		}
	}

	public void payRents() {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				c.payRents();
			}
		}
	}

	public void processHumanResources() {

		ApplicationProfiles ap = new ApplicationProfiles(game, game.getCompanies().size());
		log.debug(ap.toString());

		boolean pickedOne = true;
		while (ap.iterator().hasNext() && pickedOne) {
			log.debug("round...");
			pickedOne = false;

			callHiringProcess(ap);
			ThreadLocal.resetCompany();

			pickedOne = processApplicationOffers(ap, pickedOne);
		}

	}

	private void callHiringProcess(ApplicationProfiles ap) {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				ThreadLocal.setCompany(c);
				callHiringProcessCompany(ap, c);
			}
		}
	}

	private void callHiringProcessCompany(ApplicationProfiles ap, Company c) {
		if (c.getHumanResources().hiringProcess != null) {
			try {
				c.getHumanResources().hiringProcess.run(ap);
			} catch (RhinoException e) {
				if (!(e.getCause() instanceof GameException)) {
					game.getResult().addError(e);
					log.error("Failed to call the company.hiringProcess handler. Player " + c.getName() + " bankrupt",
							e);
					c.setBankruptFromError(e);
				}
			}
		}
	}

	private boolean processApplicationOffers(ApplicationProfiles ap, boolean pickedOne) {
		for (Iterator<ApplicationProfile> it = ap.iteratorInt(); it.hasNext();) {
			ApplicationProfile p = it.next();
			CompanyOffer co = p.getMaxOfferFor();
			if (co != null) {
				pickedOne = true;
				it.remove();
				processApplicationOffer(p, co);
			}
		}
		return pickedOne;
	}

	private void processApplicationOffer(ApplicationProfile p, CompanyOffer co) {
		Establishment est = co.getOffer().getEstablishment();
		int offer = co.getOffer().getSalary();
		if (offer >= p.getDesiredSalary()) {
			hire(p, offer, est, co.getCompany());
		} else {
			double rnd = Math.random() * p.getDesiredSalary();
			if (rnd < offer) {
				hire(p, offer, est, co.getCompany());
			}
		}
	}

	private void hire(ApplicationProfile p, int salary, Establishment est, Company company) {
		log.debug("{} hired {} for ${}", company.getName(), p, salary);
		company.getHumanResources().getEmployeesInt()
				.add(new Employee(p.getName(), est, p.getQualification(), p.getJobPosition(), salary));
	}

	public void processRealEstateBusiness() {
		RealEstateProfiles ap = new RealEstateProfiles(game, game.getCities(), game.getCompanies());
		log.debug(ap.toString());
		boolean pickedOne = true;
		while (ap.iterator().hasNext() && pickedOne) {
			pickedOne = false;

			callRealEstateAgents(ap);
			ThreadLocal.resetCompany();

			pickedOne = processRealEstateOffers(ap, pickedOne);
		}
	}

	private void callRealEstateAgents(RealEstateProfiles ap) {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				ThreadLocal.setCompany(c);
				callRealEstateAgent(ap, c);
			}
		}
	}

	private void callRealEstateAgent(RealEstateProfiles ap, Company company) {
		if (company.realEstateAgent != null) {
			try {
				company.realEstateAgent.run(ap);
			} catch (RhinoException e) {
				if (!(e.getCause() instanceof GameException)) {
					game.getResult().addError(e);
					log.error("Failed to call the company.realEstateAgent handler. Player " + company.getName()
							+ " bankrupt", e);
					company.setBankruptFromError(e);
				}
			}
		}
	}

	private boolean processRealEstateOffers(RealEstateProfiles ap, boolean pickedOne) {
		for (Iterator<RealEstateProfile> it = ap.iteratorInt(); it.hasNext();) {
			RealEstateProfile p = it.next();
			RealEstateProfiles.RealEstateOffer en = ap.getOfferFor(p);
			if (en != null) {
				it.remove();
				pickedOne = true;
				processRealEstateOffer(p, en);
			}
		}
		return pickedOne;
	}

	private void processRealEstateOffer(RealEstateProfile p, RealEstateProfiles.RealEstateOffer en) {
		try {
			en.getCompany().decCash(en.getBribe());
			game.getResult().get(en.getCompany().getName()).addTotalBribe(en.getBribe());
			Establishment est = new Establishment(en.getCompany(), p.getCity(), p.getLocationQuality(),
					p.getLocationSize(), p.getLeaseCost(), p.getSalePrice());
			en.getCompany().getEstablishmentsInt().add(est);
			if (en.isBuy()) {
				log.debug("{} bought {} for ${}", en.getCompany().getName(), p, p.getSalePrice());
				est.buy();
			} else {
				log.debug("{} rented {} for ${}", en.getCompany().getName(), p, p.getLeaseCost());
			}
		} catch (OutOfMoneyException e) {
			log.debug("Company {} is bankrupt", e.getCompany());
		}
	}

	public void close() {
		day.close();
	}

}
