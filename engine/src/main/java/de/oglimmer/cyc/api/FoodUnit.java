package de.oglimmer.cyc.api;

import java.util.Comparator;
import java.util.Set;

import lombok.Getter;

public class FoodUnit {

	@Getter
	private int units;
	@Getter
	private Food food;
	@Getter
	private int pullDate;

	public FoodUnit(Food food, int units) {
		this.food = food;
		this.units = units;
		this.pullDate = 10;
	}

	private FoodUnit(Food food, int units, int pullDate) {
		this.food = food;
		this.units = units;
		this.pullDate = pullDate;
	}

	private void decUnits() {
		units--;
	}

	void decPullDate() {
		pullDate--;
	}

	public FoodUnit split(int units) {
		if (units <= this.units) {
			FoodUnit newFU = new FoodUnit(food, units, pullDate);
			this.units -= units;
			return newFU;
		}
		return null;
	}

	public void distributeEqually() {
		Company company = ThreadLocal.getCompany();
		int numberEst = company.getEstablishments().size();
		for (int i = 0; i < numberEst; i++) {
			company.getEstablishments().get(i).sendFood(new FoodUnit(food, units / numberEst, pullDate));
		}
		units = 0;
	}

	@Override
	public String toString() {
		return "FoodUnit [units=" + units + ", food=" + food + ", pullDate=" + pullDate + "]";
	}

	static void satisfyIngredient(Set<FoodUnit> foodStores, Food ingredient) throws MissingIngredient {
		boolean done = false;
		for (FoodUnit fuS : foodStores) {
			if (fuS.getFood() == ingredient && fuS.getUnits() > 0) {
				fuS.decUnits();
				done = true;
			}
		}
		if (!done) {
			throw new MissingIngredient(ingredient);
		}
	}

	static class FoodUnitComparator implements Comparator<FoodUnit> {
		@Override
		public int compare(FoodUnit o1, FoodUnit o2) {
			if (o1.getPullDate() == o2.getPullDate()) {
				return Integer.compare(o1.hashCode(), o2.hashCode());
			}
			return Integer.compare(o1.getPullDate(), o2.getPullDate());
		}
	}
}
