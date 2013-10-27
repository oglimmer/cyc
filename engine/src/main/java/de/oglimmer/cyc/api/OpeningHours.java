package de.oglimmer.cyc.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpeningHours {

	private Logger log = LoggerFactory.getLogger(OpeningHours.class);
	private Game game;

	public OpeningHours(Game game) {
		this.game = game;
	}

	public void runBusiness() {
		for (String city : game.getCities()) {
			int totalGuests = (int) (Math.random() * game.getCompanies().size() * 100) + 50;
			log.debug("Guests for today in {}: {}", city, totalGuests);
			game.getResult().getGuestsTotalPerCity().add(city, totalGuests);

			int totalScore = 0;
			Map<Integer, Establishment> estList = new LinkedHashMap<>();
			for (Company c : game.getCompanies()) {
				if (!c.isBankrupt()) {
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
			}

			if (estList.isEmpty()) {
				log.debug("No suiteable restaurants in {}", city);
			} else {
				while (totalGuests-- > 0) {
					Guest guest = new Guest(game.getResult());
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
