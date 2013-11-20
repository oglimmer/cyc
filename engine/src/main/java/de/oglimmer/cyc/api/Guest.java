package de.oglimmer.cyc.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Guest {

	private Logger log = LoggerFactory.getLogger(Guest.class);

	private GameResult result;

	public Guest(GameResult result) {
		this.result = result;
	}

	void dine(String city, Map<Integer, Establishment> estList, int totalScore) {
		log.debug("A guest thinks about food in {}", city);
		double rnd = Math.random() * totalScore;
		for (Integer i : estList.keySet()) {
			assert rnd != -1;
			if (rnd <= i) {
				Establishment est = estList.get(i);
				log.debug("A guest decided for {} by {}", est.getAddress(), est.getParent().getName());
				serveGuest(est.getParent(), est, city);
				rnd = -1;
				break;
			}
		}
		assert rnd == -1;
	}

	private void serveGuest(Company c, Establishment est, String city) {
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
			result.get(c.getName()).addGuestsOutOfIngPerCity(city);
			result.get(c.getName()).addMissingIngredients(e.getMissingIngredients());
			log.debug("Unable to prepare meal, missing {}", e.getMissingIngredients());
		}
	}

	private void servedDish(Company c, Establishment est, MenuEntry foodSel) throws MissingIngredient {
		log.debug("Guest went to {} in {} and ordered {} for ${}", c.getName(), est.getAddress(), foodSel,
				foodSel.getPrice());
		Set<FoodUnit> foodStores = est.getStoredFoodUnitsInt();
		Set<Food> missingIngredients = new HashSet<>();
		for (Food f : foodSel.getIngredients()) {
			try {
				FoodUnit.satisfyIngredient(foodStores, f);
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
		return (Collection<MenuEntry>) GuestRule.INSTACE.selectMenu(c);
	}

}
