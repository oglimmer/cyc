package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;

import de.oglimmer.cyc.util.CountMap;

public class FoodUnitAdmin {

	private final Map<Establishment, CountMap<Food>> estFoodAvail = new HashMap<>();
	private final Map<Establishment, CountMap<Food>> estFoodUsed = new HashMap<>();

	void buildFood(Game game) {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				for (Establishment est : c.getEstablishmentsInt()) {
					CountMap<Food> map = new CountMap<>();
					for (FoodUnit fu : est.getStoredFoodUnitsInt()) {
						map.add(fu.getFood(), fu.getUnits());
					}
					estFoodAvail.put(est, map);
					estFoodUsed.put(est, new CountMap<Food>());
				}
			}
		}
	}

	void removeFood(Game game) {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt()) {
				for (Establishment est : c.getEstablishmentsInt()) {
					CountMap<Food> mapUsed = getCountMapUsed(est);
					for (FoodUnit fu : est.getStoredFoodUnitsInt()) {
						Long toRemove = mapUsed.get(fu.getFood());
						if (toRemove != null) {
							if (toRemove > fu.getUnits()) {
								toRemove = (long) fu.getUnits();
							}
							fu.decUnits(toRemove);
							mapUsed.sub(fu.getFood(), toRemove);
						}
					}
					for (Food food : mapUsed.keySet()) {
						long unitsToRemove = mapUsed.get(food);
						assert unitsToRemove == 0;
					}
				}
			}
		}
	}

	boolean checkIngredient(Establishment est, Food food) {
		return food.check(getCountMapAvail(est), getCountMapUsed(est));
	}

	void satisfyIngredient(Establishment est, Food food) {
		getCountMapUsed(est).add(food, 1L);
	}

	private CountMap<Food> getCountMapAvail(Establishment est) {
		return estFoodAvail.get(est);
	}

	private CountMap<Food> getCountMapUsed(Establishment est) {
		return estFoodUsed.get(est);
	}
}
