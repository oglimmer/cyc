package de.oglimmer.cyc.api;

import java.util.List;

import de.oglimmer.cyc.util.CountMap;

public enum Food {
	SALAD(0.1), TOMATO(0.3), ONION(0.1), BREAD(0.4), LAMB_MEAT(2), CHICKEN_MEAT(1), BEEF_MEAT(1.7), CABBAGE(0.15), SPICES(
			0.05), GARLIC_SAUCE(0.15);

	private final double basePrice;

	private Food(double basePrice) {
		this.basePrice = basePrice;
	}

	public double getBasePrice() {
		return basePrice;
	}

	boolean check(CountMap<Food> availStore, CountMap<Food> usedStore) {
		Long avail = availStore.get(this);
		Long used = usedStore.get(this);
		if (used == null) {
			used = Long.valueOf(0L);
		}
		return avail != null && avail > used;
	}

	public static boolean check(List<Food> list, Food... toSearch) {
		for (Food food : toSearch) {
			if (!check(list, food)) {
				return false;
			}
		}
		return true;
	}

	private static boolean check(List<Food> list, Food food) {
		for (Food f : list) {
			if (f == food) {
				return true;
			}
		}
		return false;
	}

	public static int count(List<Food> list, Food toSearch) {
		int count = 0;
		for (Food food : list) {
			if (food == toSearch) {
				count++;
			}
		}
		return count;
	}
}
