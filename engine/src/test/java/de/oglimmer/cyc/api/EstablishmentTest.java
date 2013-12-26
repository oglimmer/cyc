package de.oglimmer.cyc.api;

import org.junit.Assert;
import org.junit.Test;

import de.oglimmer.cyc.api.Constants.Mode;

public class EstablishmentTest {

	@Test
	public void testDistributeEquallyOne() {
		Game game = new Game(Mode.FULL);
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
		Game game = new Game(Mode.FULL);
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
		Game game = new Game(Mode.FULL);
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
	}

}
