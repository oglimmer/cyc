package de.oglimmer.cyc.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpeningHours {

	private Logger log = LoggerFactory.getLogger(OpeningHours.class);
	private Game game;
	private int baseGuests;
	private int rndGuests;

	public OpeningHours(Game game) {
		this.game = game;
		baseGuests = (int) (Math.random() * 30 + 35);
		rndGuests = (int) (Math.random() * 50 + 75);
		log.debug("Game uses rnd*numberOfCompanies*{}+{}", rndGuests, baseGuests);
	}

	public void runBusiness() {

		calcDeliciosnessStats();

		for (String city : game.getCities()) {
			processCity(city);
		}
	}

	private void processCity(String city) {
		Map<Integer, Establishment> estList = new LinkedHashMap<>();
		int totalScore = buildEstablishmentScoresMap(city, estList);

		if (estList.isEmpty()) {
			log.debug("No suiteable restaurants in {}", city);
		} else {
			processGuests(city, estList, totalScore);
		}
	}

	private void processGuests(String city, Map<Integer, Establishment> estList, int totalScore) {
		int totalGuests = (int) (Math.random() * game.getCompanies().size() * rndGuests) + baseGuests;
		log.debug("Guests for today in {}: {}", city, totalGuests);
		game.getResult().getGuestsTotalPerCity().add(city, totalGuests);
		while (totalGuests-- > 0) {
			Guest guest = new Guest(game.getResult());
			guest.dine(city, estList, totalScore);
		}
	}

	private int buildEstablishmentScoresMap(String city, Map<Integer, Establishment> estList) {
		int totalScore = 0;
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				for (Establishment est : c.getEstablishmentsInt()) {
					if (est.getAddress().startsWith(city)) {
						int score = est.getScore();
						game.getResult().get(c.getName()).addEstablishmentScore(est.getAddress(), score);
						if (score > 0) {
							estList.put(totalScore + score, est);
							totalScore += score;
						}
					}
				}
			}
		}
		return totalScore;
	}

	private void calcDeliciosnessStats() {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				for (MenuEntry me : c.getMenu()) {
					int del = me.getDeliciousness();
					game.getResult().get(c.getName()).addMenuEntryScore(me.getName(), del);
				}
			}
		}
	}
}