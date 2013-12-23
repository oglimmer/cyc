package de.oglimmer.cyc.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpeningHours {

	private final Game game;
	private final int baseGuests;
	private final int rndGuests;
	private final ThreadPoolExecutor executor;

	public OpeningHours(Game game) {
		this.game = game;
		baseGuests = game.getConstants().getBaseGuests();
		rndGuests = game.getConstants().getRndGuests();
		log.debug("Game uses rnd*numberOfCompanies*{}+{}", rndGuests, baseGuests);
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	@SneakyThrows(value = InterruptedException.class)
	public void runBusiness() {

		calcDeliciosnessStats();

		final CountDownLatch cdl = new CountDownLatch(game.getCities().size());

		try {
			for (final String city : game.getCities()) {
				Runnable worker = new Runnable() {
					@Override
					public void run() {
						processCity(city);
						cdl.countDown();
					}
				};
				executor.execute(worker);
			}

			cdl.await();

		} catch (InterruptedException e) {
			executor.shutdownNow();
			throw e;
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
		int totalGuests = (int) (((Math.random() * game.getCompanies().size() * rndGuests) + game.getCompanies().size()
				* baseGuests) / game.getCities().size());
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
						log.debug("Est score: {}:{} ({})", est.getAddress(), score, c.getName());
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

	public void close() {
		executor.shutdown();
	}
}
