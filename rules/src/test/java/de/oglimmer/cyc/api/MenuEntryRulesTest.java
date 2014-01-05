package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;

@Slf4j
@SuppressWarnings("unused")
public class MenuEntryRulesTest {

	@Test
	public void testSimpleMeal() {
		Collection<Food> ingredients = createIngredients(Food.CHICKEN_MEAT, Food.BREAD);
		Assert.assertNotEquals(0, (int) MenuEntryRule.INSTACE.getDeliciousness(ingredients));
	}

	private Collection<Food> createIngredients(Food... foods) {
		Collection<Food> ingredients = new ArrayList<>();
		for (Food food : foods) {
			ingredients.add(food);
		}
		return ingredients;
	}

}
