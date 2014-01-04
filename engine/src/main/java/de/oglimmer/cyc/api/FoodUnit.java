package de.oglimmer.cyc.api;

import java.util.Comparator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FoodUnit {

	@Getter
	private int units;
	@Getter
	private Food food;
	@Getter
	private int pullDate;

	FoodUnit(Food food, int units) {
		this.food = food;
		this.units = units;
		this.pullDate = 10;
	}

	private FoodUnit(Food food, int units, int pullDate) {
		this.food = food;
		this.units = units;
		this.pullDate = pullDate;
	}

	void decUnits(long value) {
		units -= value;
	}

	/**
	 * Increases a day for this FoodUnit.
	 * 
	 * @param est
	 *            Establishment where the FoodUnit is currently located. For logging-purposes only.
	 * @return true if the FoodUnit is empty or rotten
	 */
	boolean incDay(Establishment est) {
		if (units == 0) {
			return true;
		}

		pullDate--;
		if (pullDate == 0) {
			log.debug("Removed a rotten food-unit of {} for {} with {} in {}", food, est.getParent().getName(), units,
					est.getAddress());
			est.getParent().getGame().getResult().get(est.getParent().getName()).getTotalRottenFood()
					.add(food.toString(), units);
			units = 0;
		}
		return pullDate == 0;
	}

	public FoodUnit split(int units) {
		if (units > 0 && units < this.units) {
			FoodUnit newFU = new FoodUnit(food, units, pullDate);
			this.units -= units;
			return newFU;
		}
		return null;
	}

	public void distributeEqually() {
		Company company = ThreadLocal.getCompany();
		int numberEst = company.getEstablishmentsInt().size();
		for (int i = 0; i < numberEst; i++) {
			company.getEstablishmentsInt().get(i).sendFood(new FoodUnit(food, units / numberEst, pullDate));
		}
		units = 0;
	}

	@Override
	public String toString() {
		return "FoodUnit [units=" + units + ", food=" + food + ", pullDate=" + pullDate + "]";
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
