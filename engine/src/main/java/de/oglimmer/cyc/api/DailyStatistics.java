package de.oglimmer.cyc.api;

import java.util.Set;

import lombok.Data;
import de.oglimmer.cyc.util.CountMap;

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

	void addServedFood(String address, String name) {
		servedUnitsPerMenuMap.add(name, 1L);
		servedUnitsPerEstablishmentMap.add(address, 1L);
	}

	void addMissingIngredientsPerFood(Set<Food> missingIngredients) {
		for (Food food : missingIngredients) {
			missingIngredientsPerFoodMap.add(food.toString(), 1L);
		}
	}

	public Long getRottenUnitsPerFood(String food) {
		return rottenUnitsPerFoodMap.get(food);
	}

	public Long getRottenUnits() {
		return rottenUnitsPerFoodMap.sum();
	}

	public Long getServedUnitsPerMenu(String menu) {
		return servedUnitsPerMenuMap.get(menu);
	}

	public Long getServedUnits() {
		return servedUnitsPerMenuMap.sum();
	}

	public Long getServedUnitsPerEstablishment(String est) {
		return servedUnitsPerEstablishmentMap.get(est);
	}

	public Long getGuestsTotalPerCity(String city) {
		return guestsTotalPerCityMap.get(city);
	}

	public Long getGuestsPerEstablishment(String est) {
		return guestsPerEstablishmentMap.get(est);
	}

	public Long getGuests() {
		return guestsPerEstablishmentMap.sum();
	}

	public Long getGuestsLeftPerEstablishment(String est) {
		return guestsLeftPerEstablishmentMap.get(est);
	}

	public Long getGuestsLeft() {
		return guestsLeftPerEstablishmentMap.sum();
	}

	public Long getGuestsOutOfIngPerEstablishment(String est) {
		return guestsOutOfIngPerEstablishmentMap.get(est);
	}

	public Long getGuestsOutOfIng() {
		return guestsOutOfIngPerEstablishmentMap.sum();
	}

	public Long getMissingIngredientsPerFood(String food) {
		return missingIngredientsPerFoodMap.get(food);
	}

	public Long getMissingIngredients() {
		return missingIngredientsPerFoodMap.sum();
	}

}
