package de.oglimmer.cyc.api;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Runs multi-threaded.
 * 
 * @author oli
 *
 */
@Slf4j
public class CityProcessor implements Runnable {
	@Getter
	private City city;
	private CountDownLatch cdl;
	@Getter
	private GuestDispatcherManager guestDispMngr;
	@Getter
	private Game game;
	private FoodUnitAdmin foodUnitAdmin;
	@Getter
	private DailyStatisticsManagerCityProcessor dailyStatisticsManager = new DailyStatisticsManagerCityProcessor();

	public CityProcessor(Game game, City city, CountDownLatch cdl, FoodUnitAdmin foodUnitAdmin) {
		this.game = game;
		this.city = city;
		this.cdl = cdl;
		this.foodUnitAdmin = foodUnitAdmin;
		this.guestDispMngr = new GuestDispatcherManager(game, city);
	}

	@Override
	public void run() {
		try {
			processGuests();
			game.getDailyStatisticsManager().add(dailyStatisticsManager);
		} finally {
			cdl.countDown();
		}
	}

	private void processGuests() {
		game.getResult().getGuestsTotalPerCity().add(city.getName(), city.getGuests().size());
		for (Guest guest : city.getGuests()) {
			guest.serve(this);
		}
	}

	void serveDish(Establishment est, MenuEntry menu) throws MissingIngredient {
		Company c = est.getParent();
		log.debug("Guest went to {} in {} and ordered {} for ${}", c.getName(), est.getAddress(), menu, menu.getPrice());
		Set<Food> missingIngredients = Cooking.of(foodUnitAdmin, est, menu).cook();
		checkout(c, est, menu, missingIngredients);
	}

	private void checkout(Company company, Establishment est, MenuEntry menu, Set<Food> missingIngredients)
			throws MissingIngredient {
		if (missingIngredients == null) {
			company.incCash(menu.getPrice());
			game.getResult().getCreateNotExists(company.getName())
					.addServedFoodServed(est.getAddress(), menu.getName(), menu.getPrice());
			dailyStatisticsManager.getCollecting(company).addServedFood(est.getAddress(), menu.getName());
		} else {
			throw new MissingIngredient(missingIngredients);
		}
	}

}