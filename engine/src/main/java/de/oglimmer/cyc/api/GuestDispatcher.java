package de.oglimmer.cyc.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuestDispatcher {

	private Game game;

	@Getter
	private String city;

	private Map<Integer, Establishment> estList = new LinkedHashMap<>();

	private int totalScore;

	private final GameResult result;

	public GuestDispatcher(Game game, String city) {
		this.game = game;
		this.city = city;
		this.result = game.getResult();
		buildData();
	}

	void serveGuest() {
		Establishment est = getRandom();
		// log.debug("A guest decided for {} by {}", est.getAddress(), est.getParent().getName());
		serveGuest(est);
	}

	boolean hasRestaurants() {
		return !estList.isEmpty();
	}

	private void buildData() {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				for (Establishment est : c.getEstablishments(city)) {
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

	private Establishment getRandom() {
		double rnd = Math.random() * totalScore;
		for (Integer startScore : estList.keySet()) {
			if (rnd <= startScore) {
				return estList.get(startScore);
			}
		}
		assert false;
		return null;
	}

	private void serveGuest(Establishment est) {
		Company c = est.getParent();
		result.get(c.getName()).getGuestsYouPerCity().add(city, 1);
		try {
			if (c.getMenu().size() > 0) {
				selectMenu(c, est, city);
			}
		} catch (MissingIngredient e) {
			result.get(c.getName()).addGuestsOutOfIngPerCity(city);
			result.get(c.getName()).addMissingIngredients(e.getMissingIngredients());
			log.debug("Unable to prepare meal, missing {} in {}", e.getMissingIngredients(), est.getAddress());
		}
	}

	private void selectMenu(Company c, Establishment est, String city) throws MissingIngredient {
		Collection<MenuEntry> foodSelCol = selectMenuWrapper(c);
		if (foodSelCol.isEmpty()) {
			result.get(c.getName()).getGuestsLeftPerCity().add(city, 1);
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
		// log.debug("Guest went to {} in {} and ordered {} for ${}", c.getName(), est.getAddress(), menu,
		// menu.getPrice());
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
			result.get(company.getName()).addServedFoodServed(est.getAddress(), menu.getName(), menu.getPrice());
		} else {
			throw new MissingIngredient(missingIngredients);
		}
	}
}
