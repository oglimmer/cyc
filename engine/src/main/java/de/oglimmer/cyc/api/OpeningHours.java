package de.oglimmer.cyc.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpeningHours {

	private final Game game;
	private final ThreadPoolExecutor executor;

	public OpeningHours(Game game) {
		this.game = game;
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	@SneakyThrows(value = InterruptedException.class)
	public void runBusiness() {

		calcDeliciosnessStats();

		CountDownLatch cdl = new CountDownLatch(game.getCities().size());

		try {
			for (City city : game.getCities()) {
				executor.execute(new CityProcessor(city, cdl));
			}

			cdl.await();

		} catch (InterruptedException e) {
			executor.shutdownNow();
			throw e;
		}
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

	class CityProcessor implements Runnable {

		private City city;
		private CountDownLatch cdl;
		private GuestDispatcherManager guestDispMngr;

		public CityProcessor(City city, CountDownLatch cdl) {
			this.city = city;
			this.cdl = cdl;
			this.guestDispMngr = new GuestDispatcherManager(game, city);
		}

		@Override
		public void run() {
			processGuests();
			cdl.countDown();
		}

		private void processGuests() {
			game.getResult().getGuestsTotalPerCity().add(city.getName(), city.getGuests().size());
			for (Guest guest : city.getGuests()) {
				serveGuest(guest);
			}
		}

		private void serveGuest(Guest guest) {
			GuestDispatcher guestDisp = guestDispMngr.getDispatcher(guest);
			Establishment est = guestDisp.getRandom();
			log.debug("A guest decided for {} by {}", est.getAddress(), est.getParent().getName());
			guest.addAlreadyVisited(est.getParent());
			serveGuest(est);
		}

		private void serveGuest(Establishment est) {
			Company c = est.getParent();
			game.getResult().get(c.getName()).getGuestsYouPerCity().add(city.getName(), 1);
			try {
				if (c.getMenu().size() > 0) {
					selectMenu(c, est, city.getName());
				}
			} catch (MissingIngredient e) {
				game.getResult().get(c.getName()).addGuestsOutOfIngPerCity(city.getName());
				game.getResult().get(c.getName()).addMissingIngredients(e.getMissingIngredients());
				log.debug("Unable to prepare meal, missing {} in {}", e.getMissingIngredients(), est.getAddress());
			}
		}

		private void selectMenu(Company c, Establishment est, String city) throws MissingIngredient {
			Collection<MenuEntry> foodSelCol = selectMenuWrapper(c);
			if (foodSelCol.isEmpty()) {
				game.getResult().get(c.getName()).getGuestsLeftPerCity().add(city, 1);
				log.debug("Guest went to {} in {} and ordered nothing", c.getName(), est.getAddress());
			} else {
				for (MenuEntry menu : foodSelCol) {
					servedDish(c, est, menu);
				}
			}
		}

		@SuppressWarnings("unchecked")
		private Collection<MenuEntry> selectMenuWrapper(Company c) {
			return (Collection<MenuEntry>) GuestRule.INSTACE.selectMenu(c);
		}

		private void servedDish(Company c, Establishment est, MenuEntry menu) throws MissingIngredient {
			log.debug("Guest went to {} in {} and ordered {} for ${}", c.getName(), est.getAddress(), menu,
					menu.getPrice());
			Set<Food> missingIngredients = cook(est, menu);
			checkout(c, est, menu, missingIngredients);
		}

		private Set<Food> cook(Establishment est, MenuEntry menu) {
			Set<FoodUnit> foodStores = est.getStoredFoodUnitsInt();
			Set<Food> missingIngredients = null;
			for (Food food : menu.getIngredients()) {
				if (!FoodUnit.satisfyIngredient(foodStores, food)) {
					if (missingIngredients == null) {
						missingIngredients = new HashSet<>();
					}
					missingIngredients.add(food);
				}
			}
			return missingIngredients;
		}

		private void checkout(Company company, Establishment est, MenuEntry menu, Set<Food> missingIngredients)
				throws MissingIngredient {
			if (missingIngredients == null || missingIngredients.isEmpty()) {
				company.incCash(menu.getPrice());
				game.getResult().get(company.getName())
						.addServedFoodServed(est.getAddress(), menu.getName(), menu.getPrice());
			} else {
				throw new MissingIngredient(missingIngredients);
			}
		}

	}
}
