package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.oglimmer.cyc.util.PublicAPI;
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

	@PublicAPI
	public double getPrice(String food, int units) {
		double currentPrice = currentPrices.get(Food.valueOf(food));
		double basePrice = units * currentPrice;
		double discount = calcDiscount(units);
		log.debug("Current price for {} is ${} per unit (discount={} at {})", food, currentPrice, discount, units);
		return discount * basePrice;
	}

	private double calcDiscount(int units) {
		BulkOrderDiscount[] bulkOrderDiscounts = game.getConstants().getBulkOrderDiscounts();
		for (BulkOrderDiscount bod : bulkOrderDiscounts) {
			if (units >= bod.getStartingAmount()) {
				return bod.getDiscount();
			}
		}
		return 1;
	}

	@PublicAPI
	public void order(String food, int units) throws OutOfMoneyException {
		bulkOrder(food, units, 1);
	}

	@PublicAPI
	public void bulkOrder(String food, int unitsPerDay, int days) throws OutOfMoneyException {
		if (unitsPerDay > 0 && days > 0) {
			Company c = ThreadLocal.getCompany();
			int totalUnits = days * unitsPerDay;
			int cost = Math.max(1, (int) (getPrice(food, totalUnits)));
			c.decCash(cost);
			log.debug(c.getName() + " bought {} (over {} days) of {} for total ${}", totalUnits, days, food, cost);
			foodOrders.add(new FoodOrder(c, Food.valueOf(food), unitsPerDay, days));
			game.getResult().getCreateNotExists(c.getName()).addTotalPurchasedFood(food, totalUnits, cost);
		}
	}

	void initDay() {
		for (Food f : Food.values()) {
			currentPrices.put(f, game.getConstants().getFoodPriceChange(currentPrices.get(f)));
		}
	}

	Map<Company, FoodDelivery> createTodaysFoodDelivery() {
		Map<Company, FoodDelivery> map = new HashMap<Company, FoodDelivery>();
		for (Iterator<FoodOrder> it = foodOrders.iterator(); it.hasNext();) {
			FoodOrder fo = it.next();
			if (fo.getDays() > 1) {
				fo.decDays();
			} else {
				it.remove();
			}
			FoodUnit fu = new FoodUnit(fo.getFood(), fo.getUnits());
			FoodDelivery list = map.get(fo.getCompany());
			if (list == null) {
				list = new FoodDelivery();
				map.put(fo.getCompany(), list);
			}
			list.add(fu);
		}
		return map;
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
