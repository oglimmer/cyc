package de.oglimmer.cyc.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Guest {

	private Logger log = LoggerFactory.getLogger(Guest.class);

	private GameResult result;

	private GuestRule rule = new GuestRule();

	public Guest(GameResult result) {
		this.result = result;
	}

	void serveGuest(Company c, Establishment est, String city) {
		result.get(c.getName()).getGuestsYouPerCity().add(city, 1);
		try {
			if (c.getMenu().size() > 0) {
				Collection<MenuEntry> foodSelCol = selectMenu(c);
				if (!foodSelCol.isEmpty()) {
					for (MenuEntry foodSel : foodSelCol) {
						servedDish(c, est, foodSel);
					}
				} else {
					result.get(c.getName()).getGuestsLeftPerCity().add(city, 1);
					log.debug("Guest went to {} in {} and ordered nothing", c.getName(), est.getAddress());
				}
			}
		} catch (MissingIngredient e) {
			result.get(c.getName()).addGuestsOutOfIngPerCity(city, e.getMissingIngredients());
			log.debug("Unable to prepare meal, missing {}", e.getMissingIngredients());
		}
	}

	private void servedDish(Company c, Establishment est, MenuEntry foodSel) throws MissingIngredient {
		log.debug("Guest went to {} in {} and ordered {} for ${}", c.getName(), est.getAddress(), foodSel,
				foodSel.getPrice());
		List<FoodUnit> foodStores = est.getStoredFoodUnitsInt();
		Set<Food> missingIngredients = new HashSet<>();
		for (Food f : foodSel.getIngredients()) {
			try {
				satisfyIngredient(foodStores, f);
			} catch (MissingIngredient e) {
				missingIngredients.addAll(e.getMissingIngredients());
			}
		}
		if (missingIngredients.isEmpty()) {
			c.incCash(foodSel.getPrice());
			result.get(c.getName()).addServedFoodServed(est.getAddress(), foodSel.getName(), foodSel.getPrice());
		} else {
			throw new MissingIngredient(missingIngredients);
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<MenuEntry> selectMenu(Company c) {
		return (Collection<MenuEntry>) rule.selectMenu(c);
	}

	private void satisfyIngredient(List<FoodUnit> foodStores, Food ingredient) throws MissingIngredient {
		boolean done = false;
		for (FoodUnit fuS : foodStores) {
			if (fuS.getFood() == ingredient && fuS.getUnits() > 0) {
				fuS.decUnits();
				done = true;
			}
		}
		if (!done) {
			throw new MissingIngredient(ingredient);
		}
	}

}
