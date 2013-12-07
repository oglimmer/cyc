package de.oglimmer.cyc.api;

import lombok.extern.slf4j.Slf4j;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.WrappedException;

@Slf4j
public class Year {

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
				} catch (Exception e) {
					game.getResult().addError(e);
					log.error("Exception:Failed to call the company.launch handler. Player " + company.getName()
							+ " bankrupt", e);
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
					log.debug("Company {} paid the initial bank credit ${}", company.getName(), game.getConstants()
							.getCreditPayback());
					company.decCash(game.getConstants().getCreditPayback());
				} catch (OutOfMoneyException e) {
					log.debug("Company [] is bankrupt", e.getCompany());
				}
			}
		}
	}

}
