package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;

import de.oglimmer.cyc.util.CountMap;

public class FoodUnitAdmin {

	private final Map<Establishment, CountMap<Food>> estFoodTotals = new HashMap<>();

	void buildFood(Game game) {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				for (Establishment est : c.getEstablishmentsInt()) {
					CountMap<Food> map = new CountMap<>();
					for (FoodUnit fu : est.getStoredFoodUnitsInt()) {
						map.add(fu.getFood(), fu.getUnits());
					}
					estFoodTotals.put(est, map);
				}
			}
		}
	}

	void removeFood(Game game) {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				for (Establishment est : c.getEstablishmentsInt()) {
					CountMap<Food> map = getCountMap(est);
					for (FoodUnit fu : est.getStoredFoodUnitsInt()) {
						long toRemove = map.get(fu.getFood());
						if (toRemove > fu.getUnits()) {
							toRemove = fu.getUnits();
						}
						fu.decUnits(toRemove);
						map.sub(fu.getFood(), toRemove);
					}
					for (Food food : map.keySet()) {
						long unitsToRemove = map.get(food);
						assert unitsToRemove == 0;
					}
				}
			}
		}
	}

	boolean checkIngredient(Establishment est, Food food) {
		return checkIngredient(getCountMap(est), food);
	}

	boolean checkIngredient(CountMap<Food> map, Food food) {
		Long avail = map.get(food);
		return avail != null && avail > 0;
	}

	void satisfyIngredient(Establishment est, Food food) {
		satisfyIngredient(getCountMap(est), food);
	}

	void satisfyIngredient(CountMap<Food> map, Food food) {
		map.sub(food, 1L);
	}

	private CountMap<Food> getCountMap(Establishment est) {
		return estFoodTotals.get(est);
	}
}
