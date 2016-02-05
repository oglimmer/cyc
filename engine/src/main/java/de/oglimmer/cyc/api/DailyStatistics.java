package de.oglimmer.cyc.api;

import java.util.Map;
import java.util.Set;

import lombok.Data;
import de.oglimmer.cyc.util.CountMap;
import de.oglimmer.cyc.util.PublicAPI;
import de.oglimmer.cyc.util.SafeMap;

@Data
public class DailyStatistics {

	/*
	 * ALL ATTRIBUTES MUST BE addStats() TO THE ADD METHOD AT THE BOTTOM!!!
	 */
	
	private CountMap<String> rottenUnitsPerFoodMap = new CountMap<>();

	private CountMap<String> servedUnitsPerMenuMap = new CountMap<>();
	private CountMap<String> servedUnitsPerEstablishmentMap = new CountMap<>();

	private CountMap<String> guestsTotalPerCityMap = new CountMap<>();

	private CountMap<String> guestsPerEstablishmentMap = new CountMap<>();
	private CountMap<String> guestsLeftPerEstablishmentMap = new CountMap<>();
	private CountMap<String> guestsOutOfIngPerEstablishmentMap = new CountMap<>();

	private CountMap<String> missingIngredientsPerFoodMap = new CountMap<>();
	private SafeMap<String, CountMap<String>> missingIngredientsPerEstablishmentMap = new SafeMap<String, CountMap<String>>() {
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

	@PublicAPI
	public Long getRottenUnitsPerFood(String food) {
		Long units = rottenUnitsPerFoodMap.get(food);
		return units != null ? units : 0;
	}

	@PublicAPI
	public Long getRottenUnits() {
		return rottenUnitsPerFoodMap.sum();
	}

	@PublicAPI
	public Long getServedUnitsPerMenu(String menu) {
		Long units = servedUnitsPerMenuMap.get(menu);
		return units != null ? units : 0;
	}

	@PublicAPI
	public Long getServedUnits() {
		return servedUnitsPerMenuMap.sum();
	}

	@PublicAPI
	public Long getServedUnitsPerEstablishment(String est) {
		Long units = servedUnitsPerEstablishmentMap.get(est);
		return units != null ? units : 0; 
	}

	@PublicAPI
	public Long getGuestsTotalPerCity(String city) {
		Long guests = guestsTotalPerCityMap.get(city);
		return guests != null ? guests : 0;
	}

	@PublicAPI
	public Long getGuestsTotal() {
		return guestsTotalPerCityMap.sum();
	}

	@PublicAPI
	public Long getGuestsPerEstablishment(String est) {
		Long guests = guestsPerEstablishmentMap.get(est);
		return guests != null ? guests : 0;
	}

	@PublicAPI
	public Long getGuests() {
		return guestsPerEstablishmentMap.sum();
	}

	@PublicAPI
	public Long getGuestsLeftPerEstablishment(String est) {
		Long guests = guestsLeftPerEstablishmentMap.get(est);
		return guests != null ? guests : 0;
	}

	@PublicAPI
	public Long getGuestsLeft() {
		return guestsLeftPerEstablishmentMap.sum();
	}

	@PublicAPI
	public Long getGuestsOutOfIngPerEstablishment(String est) {
		Long guests = guestsOutOfIngPerEstablishmentMap.get(est);
		return guests != null ? guests : 0;
	}

	@PublicAPI
	public Long getGuestsOutOfIng() {
		return guestsOutOfIngPerEstablishmentMap.sum();
	}

	@PublicAPI
	public Long getMissingIngredientsPerFood(String food) {
		Long units = missingIngredientsPerFoodMap.get(food);
		return units != null ? units : 0; 
	}

	@PublicAPI
	public Long getMissingIngredients() {
		return missingIngredientsPerFoodMap.sum();
	}

	@PublicAPI
	public CountMap<String> getMissingIngredientsPerEstablishment(String estName) {
		CountMap<String> ret= missingIngredientsPerEstablishmentMap.get(estName);
		return ret;
	}

	private void addStats(DailyStatistics toAdd) {
		rottenUnitsPerFoodMap.addAll(toAdd.rottenUnitsPerFoodMap);
		servedUnitsPerMenuMap.addAll(toAdd.servedUnitsPerMenuMap);
		servedUnitsPerEstablishmentMap.addAll(toAdd.servedUnitsPerEstablishmentMap);
		guestsTotalPerCityMap.addAll(toAdd.guestsTotalPerCityMap);
		guestsPerEstablishmentMap.addAll(toAdd.guestsPerEstablishmentMap);
		guestsLeftPerEstablishmentMap.addAll(toAdd.guestsLeftPerEstablishmentMap);
		guestsOutOfIngPerEstablishmentMap.addAll(toAdd.guestsOutOfIngPerEstablishmentMap);
		missingIngredientsPerFoodMap.addAll(toAdd.missingIngredientsPerFoodMap);
		missingIngredientsPerEstablishmentMap.addAll(toAdd.missingIngredientsPerEstablishmentMap);
	}

	static void add(Map<Company, DailyStatistics> total, Map<Company, DailyStatistics> toAdd) {
		for (Map.Entry<Company, DailyStatistics> en : toAdd.entrySet()) {
			DailyStatistics ds = total.get(en.getValue());
			ds.addStats(en.getValue());
		}
	}
}
