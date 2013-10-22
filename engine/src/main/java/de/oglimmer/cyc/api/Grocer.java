package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Grocer {
	private Logger log = LoggerFactory.getLogger(Grocer.class);

	private Map<Food, Double> currentPrices = new HashMap<>();

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
		if (units > 10) {
			basePrice *= 0.99;
		} else if (units > 100) {
			basePrice *= 0.95;
		} else if (units > 500) {
			basePrice *= 0.90;
		} else if (units > 1000) {
			basePrice *= 0.85;
		} else if (units > 5000) {
			basePrice *= 0.80;
		} else if (units > 10000) {
			basePrice *= 0.70;
		}
		return basePrice;
	}

	public void order(String food, int units) throws OutOfMoneyException {
		bulkOrder(food, units, 1);
	}

	public void bulkOrder(String food, int unitsPerDay, int days) throws OutOfMoneyException {
		Company c = ThreadLocal.getCompany();
		int totalUnits = days * unitsPerDay;
		int cost = Math.max(1, (int) (getPrice(food, totalUnits)));
		c.decCash(cost);
		log.debug(c.getName() + " bought {} (over {} days) of {} for total ${}", totalUnits, days, food, cost);
		foodOrders.add(new FoodOrder(c, Food.valueOf(food), unitsPerDay, days));
		game.getResult().get(c.getName()).addTotalPurchasedFood(food, totalUnits, cost);
	}

	List<FoodOrder> getFoodOrders() {
		return foodOrders;
	}

	void initDay() {
		for (Food f : Food.values()) {
			int ud = Math.random() > 0.5 ? 1 : -1;
			double change = Math.random() / 50;
			change *= ud;
			currentPrices.put(f, currentPrices.get(f) + currentPrices.get(f) * change);
		}
	}

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

		public Company getCompany() {
			return company;
		}

		public void setCompany(Company company) {
			this.company = company;
		}

		public Food getFood() {
			return food;
		}

		public void setFood(Food food) {
			this.food = food;
		}

		public int getUnits() {
			return units;
		}

		public void setUnits(int units) {
			this.units = units;
		}

		public int getDays() {
			return days;
		}

		void decDays() {
			this.days--;
		}
	}
}
