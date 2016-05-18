package de.oglimmer.cyc.api;

import de.oglimmer.cyc.DataBackendMemory;
import de.oglimmer.cyc.api.Constants.Mode;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class EstablishmentTest {

	@Test
	public void testDistributeEquallyOne() {
		Game game = new Game(Mode.FULL, DataBackendMemory.INSTANCE);
		Company company = new Company(game, "companyA", game.getGrocer());
		Establishment est = new Establishment(company, "cityA", 5, 50, 1000, 2000);
		company.getEstablishmentsInt().add(est);
		FoodUnit fu = new FoodUnit(Food.BEEF_MEAT, 10);
		ThreadLocal.setCompany(company);
		fu.distributeEqually();

		Assert.assertEquals(1, est.getStoredFoodUnits().size());

		FoodUnit fuNew = est.getStoredFoodUnits().iterator().next();

		Assert.assertEquals(fu.getFood(), fuNew.getFood());
		Assert.assertEquals(10, fuNew.getUnits());
		Assert.assertEquals(0, fu.getUnits());
		Assert.assertEquals(fu.getPullDate(), fuNew.getPullDate());
	}

	@Test
	public void testDistributeEquallyTwo() {
		Game game = new Game(Mode.FULL, DataBackendMemory.INSTANCE);
		Company company = new Company(game, "companyA", game.getGrocer());
		Establishment est1 = new Establishment(company, "cityA", 5, 50, 1000, 2000);
		Establishment est2 = new Establishment(company, "cityA", 5, 50, 1000, 2000);
		company.getEstablishmentsInt().add(est1);
		company.getEstablishmentsInt().add(est2);
		FoodUnit fu = new FoodUnit(Food.BEEF_MEAT, 10);
		ThreadLocal.setCompany(company);
		fu.distributeEqually();

		Assert.assertEquals(1, est1.getStoredFoodUnits().size());
		Assert.assertEquals(1, est2.getStoredFoodUnits().size());

		FoodUnit fuNew1 = est1.getStoredFoodUnits().iterator().next();
		FoodUnit fuNew2 = est2.getStoredFoodUnits().iterator().next();
		Assert.assertEquals(0, fu.getUnits());

		Assert.assertEquals(fu.getFood(), fuNew1.getFood());
		Assert.assertEquals(5, fuNew1.getUnits());
		Assert.assertEquals(fu.getPullDate(), fuNew1.getPullDate());
		Assert.assertEquals(fu.getFood(), fuNew2.getFood());
		Assert.assertEquals(5, fuNew2.getUnits());
		Assert.assertEquals(fu.getPullDate(), fuNew2.getPullDate());
	}

	@Test
	public void testCleanFoodStorage() {
		Thread.currentThread().setName("TestRun-0");
		Game game = new Game(Mode.FULL, DataBackendMemory.INSTANCE);
		Company company = new Company(game, "companyA", game.getGrocer());
		Establishment est = new Establishment(company, "cityA", 5, 50, 1000, 2000);
		company.getEstablishmentsInt().add(est);
		FoodUnit fu = new FoodUnit(Food.BEEF_MEAT, 10);
		ThreadLocal.setCompany(company);
		fu.distributeEqually();

		FoodUnit fuEst = est.getStoredFoodUnits().iterator().next();
		for (int i = 0; i < 9; i++) {
			Assert.assertEquals(10 - i, fuEst.getPullDate());
			est.cleanFoodStorage();
			Assert.assertEquals(9 - i, fuEst.getPullDate());
			Assert.assertEquals(1, est.getStoredFoodUnits().size());
		}
		est.cleanFoodStorage();
		Assert.assertEquals(0, fuEst.getPullDate());
		Assert.assertEquals(0, est.getStoredFoodUnits().size());
		Assert.assertEquals(10,
				game.getGameRun().getResult().getCreateNotExists("companyA").getTotalRottenFood().get(Food.BEEF_MEAT.toString())
						.val);
	}

	@Test
	public void testNewFoodUnit() {
		Game game = new Game(Mode.FULL, DataBackendMemory.INSTANCE);
		Company company = new Company(game, "companyA", game.getGrocer());
		Establishment est = new Establishment(company, "cityA", 5, 50, 1000, 2000);
		company.getEstablishmentsInt().add(est);
		ThreadLocal.setCompany(company);

		// add 10 units on day-1

		FoodUnit fuIn1 = new FoodUnit(Food.BEEF_MEAT, 10);
		fuIn1.distributeEqually();

		est.cleanFoodStorage();

		// add 10 units on day-2

		FoodUnit fu2 = new FoodUnit(Food.BEEF_MEAT, 10);
		fu2.distributeEqually();

		est.cleanFoodStorage();

		// add 10 units on day-3

		FoodUnit fu3 = new FoodUnit(Food.BEEF_MEAT, 10);
		fu3.distributeEqually();

		// we now have pull dates = 8,9,10 in the Set

		int day = 8;
		for (FoodUnit fu : est.getStoredFoodUnitsInt()) {
			Assert.assertEquals(day, fu.getPullDate());
			day++;
		}

		// take the first FU (pulldate=8) and move it (will be added to the back of the list)

		est.getStoredFoodUnitsInt().iterator().next().distributeEqually();
		est.cleanFoodStorage();

		// we now have pull dates = 7,8,9 in the Set

		day = 7;
		for (FoodUnit fu : est.getStoredFoodUnitsInt()) {
			Assert.assertEquals(day, fu.getPullDate());
			day++;
		}

		// take the second FU (pulldate=8) and move it (will be added to the back of the list)

		Iterator<FoodUnit> it = est.getStoredFoodUnitsInt().iterator();
		it.next();
		it.next().distributeEqually();

		est.cleanFoodStorage();

		// we now have pull dates = 6,7,8 in the Set

		day = 6;
		for (FoodUnit fu : est.getStoredFoodUnitsInt()) {
			Assert.assertEquals(day, fu.getPullDate());
			day++;
		}

	}

	@Test
	public void testInteriorAccessoriesIntegerOverflow() {
		Game game = new Game(Mode.FULL, DataBackendMemory.INSTANCE);
		Company company = new Company(game, "companyA", game.getGrocer());
		Establishment est = new Establishment(company, "cityA", 5, 50, 1000, 2000);
		company.getEstablishmentsInt().add(est);
		ThreadLocal.setCompany(company);

		ArrayList<String> accessories = new ArrayList<>(429498 * 2);
		for (int i = 0; i < 429498 * 2; i++) {
			accessories.add(InteriorAccessory.OVEN.toString());
		}
		est.buyInteriorAccessories(accessories.toArray(new String[0]));

		Assert.assertNotEquals(accessories.size(), est.getInteriorAccessories().size());
	}

}
