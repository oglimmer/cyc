package de.oglimmer.cyc.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class MenuEntrySecret implements IMenuEntrySecret {

	private MenuEntry menuEntry;

	/**
	 * a value of 1 is a perfect price<br/>
	 * a value of < 1 means it is too expensive<br/>
	 * a value of > 1 means it is too cheap<br/>
	 * 0 ... 6.23
	 */
	public double getValueForMoneyScore() {
		double netCost = 0;
		for (Food f : menuEntry.getIngredientsInt()) {
			netCost += f.getBasePrice();
		}
		double valForMon = Math.pow(2,
				menuEntry.getGame().getConstants().getMenuPriceFactor() * menuEntry.getPrice() / netCost + 2.64094)
				- 0.00419190479;
		log.debug("{} valForMoney={} ({}/{})", menuEntry.getName(), valForMon, netCost, menuEntry.getPrice());
		return valForMon;
	}

	/**
	 * base deliciousness is 5. max 10, min 0.
	 */
	public int getDeliciousness() {
		int del = menuEntry.getCache().getValue();
		log.debug("{} deli={}", menuEntry.getName(), del);
		return del;
	}
}
