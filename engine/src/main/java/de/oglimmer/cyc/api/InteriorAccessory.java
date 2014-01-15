package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
		return list.containsAll(Arrays.asList(toSearch));
	}

	public static int count(List<InteriorAccessory> list, InteriorAccessory toSearch) {
		return Collections.frequency(list, toSearch);
	}

	public static Collection<InteriorAccessory> valuesOf(Collection<InteriorAccessory> excludeCol, String... intAccies) {
		Collection<InteriorAccessory> convertedCol = new ArrayList<>(intAccies.length);
		for (String intAccStr : intAccies) {
			InteriorAccessory intAcc = InteriorAccessory.valueOf(intAccStr);
			if (excludeCol == null || !excludeCol.contains(intAcc)) {
				convertedCol.add(intAcc);
			}
		}
		return convertedCol;
	}
}
