package de.oglimmer.cyc.api;

import java.util.List;

import lombok.Getter;

public enum InteriorAccessory {

	TABLE(500), CHAIR(100), COUNTER(2500), VERTICAL_ROTISSERIE(1500), TOASTER(700), OVEN(5000), COFFEE_MACHINE(2000), BEVERAGE_COOLER(
			1500), FRIDGE(3500);

	@Getter
	private final int assetCost;

	private InteriorAccessory(int assetCost) {
		this.assetCost = assetCost;
	}

	public static boolean check(List<InteriorAccessory> list, InteriorAccessory... toSearch) {
		for (InteriorAccessory toS : toSearch) {
			boolean isThere = false;
			for (InteriorAccessory ia : list) {
				if (ia == toS) {
					isThere = true;
				}
			}
			if (!isThere) {
				return false;
			}
		}
		return true;
	}

	public static int count(List<InteriorAccessory> list, InteriorAccessory toSearch) {
		int count = 0;
		for (InteriorAccessory ia : list) {
			if (ia == toSearch) {
				count++;
			}
		}
		return count;
	}
}
