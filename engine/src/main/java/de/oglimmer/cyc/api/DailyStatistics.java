package de.oglimmer.cyc.api;

import java.util.Map;
import java.util.Set;

import lombok.Data;
import de.oglimmer.cyc.util.CountMap;
import de.oglimmer.cyc.util.SafeMap;

@Data
public class DailyStatistics {

	private CountMap<String> rottenUnitsPerFoodMap = new CountMap<>();

	private CountMap<String> servedUnitsPerMenuMap = new CountMap<>();
	private CountMap<String> servedUnitsPerEstablishmentMap = new CountMap<>();

	private CountMap<String> guestsTotalPerCityMap = new CountMap<>();

	private CountMap<String> guestsPerEstablishmentMap = new CountMap<>();
	private CountMap<String> guestsLeftPerEstablishmentMap = new CountMap<>();
	private CountMap<String> guestsOutOfIngPerEstablishmentMap = new CountMap<>();

	private CountMap<String> missingIngredientsPerFoodMap = new CountMap<>();
	private Map<String, CountMap<String>> missingIngredientsPerEstablishmentMap = new SafeMap<String, CountMap<String>>() {
		private static final long serialVersionUID = 1L;

		@Override
		protected CountMap<String> createValue() {
			return new CountMap<String>();
		}
	};

	void addServedFood(String address, String name) {
		servedUnitsPerMenuMap.add(name, 1L);
		servedUnitsPerEstablishmentMap.add(address, 1L);
	}

	void addMissingIngredientsPerFood(Set<Food> missingIngredients, Establishment est) {
		addMissingIngredientsPerFoodToMap(missingIngredientsPerFoodMap, missingIngredients);
		CountMap<String> cm = missingIngredientsPerEstablishmentMap.get(est.getAddress());
		addMissingIngredientsPerFoodToMap(cm, missingIngredients);
	}

	private void addMissingIngredientsPerFoodToMap(CountMap<String> targetMap, Set<Food> missingIngredients) {
		for (Food food : missingIngredients) {
			targetMap.add(food.toString(), 1L);
		}
	}

	public Long getRottenUnitsPerFood(String food) {
		Long units = rottenUnitsPerFoodMap.get(food);
		return units != null ? units : 0;
	}

	public Long getRottenUnits() {
		return rottenUnitsPerFoodMap.sum();
	}

	public Long getServedUnitsPerMenu(String menu) {
		Long units = servedUnitsPerMenuMap.get(menu);
		return units != null ? units : 0;
	}

	public Long getServedUnits() {
		return servedUnitsPerMenuMap.sum();
	}

	public Long getServedUnitsPerEstablishment(String est) {
		Long units = servedUnitsPerEstablishmentMap.get(est);
		return units != null ? units : 0; 
	}

	public Long getGuestsTotalPerCity(String city) {
		Long guests = guestsTotalPerCityMap.get(city);
		return guests != null ? guests : 0;
	}

	public Long getGuestsTotal() {
		return guestsTotalPerCityMap.sum();
	}

	public Long getGuestsPerEstablishment(String est) {
		Long guests = guestsPerEstablishmentMap.get(est);
		return guests != null ? guests : 0;
	}

	public Long getGuests() {
		return guestsPerEstablishmentMap.sum();
	}

	public Long getGuestsLeftPerEstablishment(String est) {
		Long guests = guestsLeftPerEstablishmentMap.get(est);
		return guests != null ? guests : 0;
	}

	public Long getGuestsLeft() {
		return guestsLeftPerEstablishmentMap.sum();
	}

	public Long getGuestsOutOfIngPerEstablishment(String est) {
		Long guests = guestsOutOfIngPerEstablishmentMap.get(est);
		return guests != null ? guests : 0;
	}

	public Long getGuestsOutOfIng() {
		return guestsOutOfIngPerEstablishmentMap.sum();
	}

	public Long getMissingIngredientsPerFood(String food) {
		Long units = missingIngredientsPerFoodMap.get(food);
		return units != null ? units : 0; 
	}

	public Long getMissingIngredients() {
		return missingIngredientsPerFoodMap.sum();
	}

	public CountMap<String> getMissingIngredientsPerEstablishment(String estName) {
		CountMap<String> ret= missingIngredientsPerEstablishmentMap.get(estName);
		System.out.println(ret);
		return ret;
	}
}
