package de.oglimmer.cyc.api;

import lombok.extern.slf4j.Slf4j;

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
				company.callLaunch();
			}
		}
	}

	private void payCredits() {
		for (Company company : game.getCompanies()) {
			if (!company.isBankrupt()) {
				company.payCredit();
			}
		}
	}

	public void close() {
		month.close();
	}

}
