package de.oglimmer.cyc.api;

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;
import de.oglimmer.cyc.api.ApplicationProfile.CompanyOffer;
import de.oglimmer.cyc.api.RealEstateProfile.RealEstateOffer;

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
				company.callMonthly();
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
		while (ap.size() > 0 && pickedOne) {
			callHiringProcess(ap);
			pickedOne = processApplicationOffers(ap);
		}
	}

	private void callHiringProcess(ApplicationProfiles ap) {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				c.callHiringProcessCompany(ap);
			}
		}
	}

	private boolean processApplicationOffers(ApplicationProfiles ap) {
		boolean pickedOne = false;
		for (Iterator<ApplicationProfile> it = ap.iteratorInt(); it.hasNext();) {
			ApplicationProfile p = it.next();
			CompanyOffer co = p.getMaxOfferFor();
			if (co != null) {
				pickedOne = true;
				it.remove();
				initiateHiring(p, co);
			}
		}
		return pickedOne;
	}

	private void initiateHiring(ApplicationProfile p, CompanyOffer co) {
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
			callRealEstateAgents(ap);
			pickedOne = processRealEstateProfiles(ap);
		}
	}

	private void callRealEstateAgents(RealEstateProfiles ap) {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				c.callRealEstateAgent(ap);
			}
		}
	}

	private boolean processRealEstateProfiles(RealEstateProfiles ap) {
		boolean pickedOne = false;
		for (Iterator<RealEstateProfile> it = ap.iteratorInt(); it.hasNext();) {
			RealEstateProfile p = it.next();
			RealEstateOffer reo = p.getMaxOfferFor();
			if (reo != null) {
				it.remove();
				pickedOne = true;
				finalizeRealEstateBusiness(p, reo);
			}
		}
		return pickedOne;
	}

	private void finalizeRealEstateBusiness(RealEstateProfile p, RealEstateOffer reo) {
		try {
			reo.getCompany().decCash(reo.getOffer().getBribe());
			game.getResult().get(reo.getCompany().getName()).addTotalBribe(reo.getOffer().getBribe());
			Establishment est = new Establishment(reo.getCompany(), p.getCity(), p.getLocationQuality(),
					p.getLocationSize(), p.getLeaseCost(), p.getSalePrice());
			reo.getCompany().getEstablishmentsInt().add(est);
			if (reo.getOffer().isBuy()) {
				log.debug("{} bought {} for ${}", reo.getCompany().getName(), p, p.getSalePrice());
				est.buy();
			} else {
				log.debug("{} rented {} for ${}", reo.getCompany().getName(), p, p.getLeaseCost());
			}
			game.createCities();
		} catch (OutOfMoneyException e) {
			log.debug("Company {} is bankrupt", e.getCompany());
		}
	}

	public void close() {
		day.close();
	}

}
