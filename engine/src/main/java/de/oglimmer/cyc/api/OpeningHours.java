package de.oglimmer.cyc.api;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import de.oglimmer.cyc.util.NamedThreadFactory;
import lombok.SneakyThrows;

/**
 * Runs in a single thread.
 * 
 * @author oli
 *
 */
public class OpeningHours {

	private final Game game;
	private final ThreadPoolExecutor executor;
	private final FoodUnitAdmin foodUnitAdmin;

	public OpeningHours(Game game) {
		this.game = game;
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
				new NamedThreadFactory("CityProcessor"));
		foodUnitAdmin = new FoodUnitAdmin();
	}

	@SneakyThrows(value = InterruptedException.class)
	public void runBusiness() {

		calcDeliciosnessStats();

		foodUnitAdmin.buildFood(game);

		CountDownLatch cdl = new CountDownLatch(game.getCities().size());

		try {
			for (City city : game.getCities()) {
				executor.execute(new CityProcessor(game, city, cdl, foodUnitAdmin));
			}

			cdl.await();

		} catch (InterruptedException e) {
			executor.shutdownNow();
			throw e;
		}

		foodUnitAdmin.removeFood(game);
	}

	private void calcDeliciosnessStats() {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				for (MenuEntry me : c.getMenu()) {
					int del = (int) (me.getSecret().getDeliciousness() * me.getSecret().getValueForMoneyScore());
					game.getResult().getCreateNotExists(c.getName()).addMenuEntryScore(me.getName(), del);
				}
			}
		}
	}

	public void close() {
		executor.shutdown();
	}

}
