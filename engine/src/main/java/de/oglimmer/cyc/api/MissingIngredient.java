package de.oglimmer.cyc.api;

import java.util.HashSet;
import java.util.Set;

public class MissingIngredient extends Exception {

	private static final long serialVersionUID = 1L;
	private Set<Food> missingIngredients;

	public MissingIngredient(Food ingredient) {
		missingIngredients = new HashSet<>();
		missingIngredients.add(ingredient);
	}

	public MissingIngredient(Set<Food> missingIngredients) {
		this.missingIngredients = missingIngredients;
	}

	public Set<Food> getMissingIngredients() {
		return missingIngredients;
	}
}