package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Grocer {

	private Map<Food, Double> currentPrices = new HashMap<>();

	@Getter(AccessLevel.PACKAGE)
	private List<FoodOrder> foodOrders = new ArrayList<>();

	private Game game;

	public Grocer(Game game) {
		this.game = game;
		for (Food f : Food.values()) {
			currentPrices.put(f, f.getBasePrice());
		}
	}

	public double getPrice(String food, int units) {
		log.debug("Current price for {} is ${} per unit", food, currentPrices.get(Food.valueOf(food)));
		double basePrice = units * currentPrices.get(Food.valueOf(food));
		BulkOrderDiscount[] bulkOrderDiscounts = Constants.INSTACE.getBulkOrderDiscounts();
		for (BulkOrderDiscount bod : bulkOrderDiscounts) {
			if (units >= bod.getStartingAmount()) {
				basePrice *= bod.getDiscount();
				break;
			}
		}
		return basePrice;
	}

	public void order(String food, int units) throws OutOfMoneyException {
		bulkOrder(food, units, 1);
	}

	public void bulkOrder(String food, int unitsPerDay, int days) throws OutOfMoneyException {
		if (unitsPerDay > 0 && days > 0) {
			Company c = ThreadLocal.getCompany();
			int totalUnits = days * unitsPerDay;
			int cost = Math.max(1, (int) (getPrice(food, totalUnits)));
			c.decCash(cost);
			log.debug(c.getName() + " bought {} (over {} days) of {} for total ${}", totalUnits, days, food, cost);
			foodOrders.add(new FoodOrder(c, Food.valueOf(food), unitsPerDay, days));
			game.getResult().get(c.getName()).addTotalPurchasedFood(food, totalUnits, cost);
		}
	}

	void initDay() {
		for (Food f : Food.values()) {
			currentPrices.put(f, Constants.INSTACE.getFoodPriceChange(currentPrices.get(f)));
		}
	}

	@Data
	class FoodOrder {
		private Company company;
		private Food food;
		private int units;
		private int days;

		public FoodOrder(Company c, Food food, int units, int days) {
			this.company = c;
			this.food = food;
			this.units = units;
			this.days = days;
		}

		void decDays() {
			this.days--;
		}
	}

	@Value
	static class BulkOrderDiscount {
		private int startingAmount;
		private double discount;
	}
}
