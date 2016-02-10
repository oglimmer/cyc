package de.oglimmer.cyc.api;

import java.util.Collections;
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

	/**
	 * Returns true if the value for this Food in the availStore map is larger than in the usedStore map
	 * 
	 * @param availStore
	 *            holds the available food
	 * @param usedStore
	 *            holds the already used food
	 * @return true if availStore.get(this) > usedStore.get(this)
	 */
	boolean check(CountMap<Food> availStore, CountMap<Food> usedStore) {
		long avail = availStore.getLong(this);
		long used = usedStore.getLong(this);
		return avail > used;
	}

	public int count(List<Food> list) {
		return Collections.frequency(list, this);
	}
}
