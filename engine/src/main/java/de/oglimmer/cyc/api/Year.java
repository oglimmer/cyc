package de.oglimmer.cyc.api;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.WrappedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Year {

	private Logger log = LoggerFactory.getLogger(Year.class);

	private Month month;
	private Game game;

	public Year(Game game) {
		this.game = game;
		month = new Month(game);
	}

	void processYear(int year) {

		callLaunch();

		for (int monthCount = 1; monthCount <= game.getTotalMonth(); monthCount++) {
			log.debug("Year: {}", year);
			month.processMonth(monthCount);
		}

		if (year == 1) {
			payCredits();
		}
	}

	private void callLaunch() {
		for (Company company : game.getCompanies()) {
			if (!company.isBankrupt()) {
				try {
					if (company.launch != null) {
						ThreadLocal.setCompany(company);
						company.launch.run();
					}
				} catch (WrappedException e) {
					if (!(e.getCause() instanceof GameException)) {
						game.getResult().addError(e);
						log.error("Failed to call the company.launch handler", e);
					}
				} catch (EcmaError e) {
					game.getResult().addError(e);
					log.error("Failed to call the company.launch handler. Player " + company.getName() + " bankrupt", e);
					company.setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}

	private void payCredits() {
		for (Company company : game.getCompanies()) {
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

}
