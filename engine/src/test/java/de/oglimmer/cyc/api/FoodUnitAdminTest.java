package de.oglimmer.cyc.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.oglimmer.cyc.DataBackendMemory;
import de.oglimmer.cyc.api.Constants.Mode;

public class FoodUnitAdminTest {

	private Game game;
	private Establishment est;

	@Before
	public void warmUp() {
		game = new Game(Mode.FULL, DataBackendMemory.INSTANCE);
		Company c = new Company(game, "companyA", game.getGrocer());
		est = new Establishment(c, "cityA", 5, 50, 5000, 50000);
		c.getEstablishmentsInt().add(est);
		game.getCompanies().add(c);

		FoodUnit fuBeef1 = new FoodUnit(Food.BEEF_MEAT, 2);
		est.sendFood(fuBeef1);
		FoodUnit fuCabbage = new FoodUnit(Food.CABBAGE, 3);
		est.sendFood(fuCabbage);
		FoodUnit fuBeef2 = new FoodUnit(Food.BEEF_MEAT, 1);
		est.sendFood(fuBeef2);

	}

	private void validate(int expectedCabbage, int expectedMeat) {
		int cabbage = 0;
		int meat = 0;
		for (FoodUnit fu : est.getStoredFoodUnitsInt()) {
			if (fu.getFood() == Food.CABBAGE) {
				cabbage += fu.getUnits();
			} else if (fu.getFood() == Food.BEEF_MEAT) {
				meat += fu.getUnits();
			}
		}
		Assert.assertEquals(expectedCabbage, cabbage);
		Assert.assertEquals(expectedMeat, meat);
	}

	@Test
	public void nop() {
		FoodUnitAdmin fua = new FoodUnitAdmin();

		fua.buildFood(game);
		fua.removeFood(game);

		validate(3, 3);
	}

	@Test
	public void usageCabbage2() {
		FoodUnitAdmin fua = new FoodUnitAdmin();

		fua.buildFood(game);
		Assert.assertTrue(fua.checkIngredient(est, Food.CABBAGE));
		fua.satisfyIngredient(est, Food.CABBAGE);
		Assert.assertTrue(fua.checkIngredient(est, Food.CABBAGE));
		fua.satisfyIngredient(est, Food.CABBAGE);
		Assert.assertTrue(fua.checkIngredient(est, Food.CABBAGE));
		fua.removeFood(game);

		validate(1, 3);
	}

	@Test
	public void usageCabbage3() {
		FoodUnitAdmin fua = new FoodUnitAdmin();

		fua.buildFood(game);
		Assert.assertTrue(fua.checkIngredient(est, Food.CABBAGE));
		fua.satisfyIngredient(est, Food.CABBAGE);
		Assert.assertTrue(fua.checkIngredient(est, Food.CABBAGE));
		fua.satisfyIngredient(est, Food.CABBAGE);
		Assert.assertTrue(fua.checkIngredient(est, Food.CABBAGE));
		fua.satisfyIngredient(est, Food.CABBAGE);
		Assert.assertFalse(fua.checkIngredient(est, Food.CABBAGE));
		fua.removeFood(game);

		validate(0, 3);
	}

	@Test
	public void usageBeef1() {
		FoodUnitAdmin fua = new FoodUnitAdmin();

		fua.buildFood(game);
		Assert.assertTrue(fua.checkIngredient(est, Food.BEEF_MEAT));
		fua.satisfyIngredient(est, Food.BEEF_MEAT);
		Assert.assertTrue(fua.checkIngredient(est, Food.BEEF_MEAT));
		fua.removeFood(game);

		validate(3, 2);
	}

	@Test
	public void usageBeef2() {
		FoodUnitAdmin fua = new FoodUnitAdmin();

		fua.buildFood(game);
		Assert.assertTrue(fua.checkIngredient(est, Food.BEEF_MEAT));
		fua.satisfyIngredient(est, Food.BEEF_MEAT);
		Assert.assertTrue(fua.checkIngredient(est, Food.BEEF_MEAT));
		fua.satisfyIngredient(est, Food.BEEF_MEAT);
		Assert.assertTrue(fua.checkIngredient(est, Food.BEEF_MEAT));
		fua.removeFood(game);

		validate(3, 1);
	}

	@Test
	public void usageBeef3() {
		FoodUnitAdmin fua = new FoodUnitAdmin();

		fua.buildFood(game);
		Assert.assertTrue(fua.checkIngredient(est, Food.BEEF_MEAT));
		fua.satisfyIngredient(est, Food.BEEF_MEAT);
		Assert.assertTrue(fua.checkIngredient(est, Food.BEEF_MEAT));
		fua.satisfyIngredient(est, Food.BEEF_MEAT);
		Assert.assertTrue(fua.checkIngredient(est, Food.BEEF_MEAT));
		fua.satisfyIngredient(est, Food.BEEF_MEAT);
		Assert.assertFalse(fua.checkIngredient(est, Food.BEEF_MEAT));
		fua.removeFood(game);

		validate(3, 0);
	}

}
