package de.oglimmer.cyc.api;

public class FoodUnit {

	private int units;
	private Food food;
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

	public int getUnits() {
		return units;
	}

	public Food getFood() {
		return food;
	}

	public int getPullDate() {
		return pullDate;
	}

	void decUnits() {
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

}
