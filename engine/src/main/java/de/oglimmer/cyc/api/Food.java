package de.oglimmer.cyc.api;

import java.util.List;

public enum Food {
	SALAD(0.1), TOMATO(0.3), ONION(0.1), BREAD(0.4), LAMB_MEAT(2), CHICKEN_MEAT(1), BEEF_MEAT(1.7), CABBAGE(0.15), SPICES(
			0.05), GARLIC_SAUCE(0.15);

	private final double basePrice;

	private Food(double basePrice) {
		this.basePrice = basePrice;
	}

	double getBasePrice() {
		return basePrice;
	}

	public static boolean check(List<Food> list, Food... toSearch) {
		for (Food food : toSearch) {
			boolean isThere = false;
			for (Food f : list) {
				if (f == food) {
					isThere = true;
				}
			}
			if (!isThere) {
				return false;
			}
		}
		return true;
	}

	public static int count(List<Food> list, Food toSearch) {
		int count = 0;
		for (Food ia : list) {
			if (ia == toSearch) {
				count++;
			}
		}
		return count;
	}
}
