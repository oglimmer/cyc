package de.oglimmer.cyc.api;

import java.util.HashSet;
import java.util.Set;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class Cooking {

	@NonNull
	private FoodUnitAdmin foodUnitAdmin;

	@NonNull
	private Establishment est;

	@NonNull
	private MenuEntry menu;

	private Set<Food> missingIngredients;

	Set<Food> cook() {
		checkAvailabilityIngredients();
		removeIngredients();
		return missingIngredients;
	}

	private void checkAvailabilityIngredients() {
		for (Food food : menu.getIngredientsInt()) {
			checkAvailabilityIngredient(food);
		}
	}

	private void checkAvailabilityIngredient(Food food) {
		if (!foodUnitAdmin.checkIngredient(est, food)) {
			addMissingIngredient(food);
		}
	}

	private void addMissingIngredient(Food food) {
		if (missingIngredients == null) {
			missingIngredients = new HashSet<>();
		}
		missingIngredients.add(food);
	}

	private void removeIngredients() {
		if (isAllIngredientsAvailable()) {
			for (Food food : menu.getIngredientsInt()) {
				foodUnitAdmin.satisfyIngredient(est, food);
			}
		}
	}

	private boolean isAllIngredientsAvailable() {
		return missingIngredients == null;
	}

}
