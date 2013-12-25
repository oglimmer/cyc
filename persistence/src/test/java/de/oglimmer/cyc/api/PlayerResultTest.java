package de.oglimmer.cyc.api;

import java.util.ArrayList;

import lombok.val;

import org.junit.Assert;
import org.junit.Test;

public class PlayerResultTest {

	@Test
	public void addTotalOnRentTest() {
		val pr = new PlayerResult();
		pr.addTotalOnRent(5);
		pr.addTotalOnRent(7);
		pr.addTotalOnRent(9);
		Assert.assertEquals(21, pr.getTotalOnRent());
	}

	@Test
	public void addTotalRealEstateTest() {
		val pr = new PlayerResult();
		pr.addTotalRealEstate(5);
		pr.addTotalRealEstate(7);
		pr.addTotalRealEstate(9);
		Assert.assertEquals(21, pr.getTotalRealEstate());
	}

	@Test
	public void addEstablishmentsByDaysTest() {
		val pr = new PlayerResult();
		pr.addEstablishmentsByDays(5);
		pr.addEstablishmentsByDays(7);
		pr.addEstablishmentsByDays(9);
		Assert.assertEquals(21, pr.getEstablishmentsByDays());
	}

	@Test
	public void addStaffByDaysTest() {
		val pr = new PlayerResult();
		pr.addStaffByDays("A");
		pr.addStaffByDays("A");
		pr.addStaffByDays("A");
		pr.addStaffByDays("B");
		Assert.assertEquals(3, (long) pr.getStaffByDays().get("A"));
		Assert.assertEquals(1, (long) pr.getStaffByDays().get("B"));
	}

	@Test
	public void addTotalOnSalariesTest() {
		val pr = new PlayerResult();
		pr.addTotalOnSalaries("A", 10);
		pr.addTotalOnSalaries("A", 20);
		pr.addTotalOnSalaries("A", 30);
		pr.addTotalOnSalaries("B", 20);
		Assert.assertEquals(60, (long) pr.getTotalOnSalaries().get("A"));
		Assert.assertEquals(20, (long) pr.getTotalOnSalaries().get("B"));
	}

	@Test
	public void getSalariesTotalTest() {
		val pr = new PlayerResult();
		pr.addTotalOnSalaries("A", 10);
		pr.addTotalOnSalaries("A", 20);
		pr.addTotalOnSalaries("A", 30);
		pr.addTotalOnSalaries("B", 20);
		Assert.assertEquals(80, pr.getSalariesTotal());
	}

	@Test
	public void addTotalPurchasedFoodTest() {
		val pr = new PlayerResult();
		pr.addTotalPurchasedFood("A", 10, 100);
		pr.addTotalPurchasedFood("A", 20, 200);
		pr.addTotalPurchasedFood("A", 30, 300);
		pr.addTotalPurchasedFood("B", 20, 300);
		Assert.assertEquals(60, (long) pr.getTotalPurchasedFoodUnits().get("A"));
		Assert.assertEquals(600, (long) pr.getTotalPurchasedFoodCosts().get("A"));
		Assert.assertEquals(20, (long) pr.getTotalPurchasedFoodUnits().get("B"));
		Assert.assertEquals(300, (long) pr.getTotalPurchasedFoodCosts().get("B"));
	}

	@Test
	public void getPurchasedFoodCostsTotalTest() {
		val pr = new PlayerResult();
		pr.addTotalPurchasedFood("A", 10, 100);
		pr.addTotalPurchasedFood("A", 20, 200);
		pr.addTotalPurchasedFood("A", 30, 300);
		pr.addTotalPurchasedFood("B", 20, 300);
		Assert.assertEquals(900, pr.getPurchasedFoodCostsTotal());
	}

	private PlayerResult createPlayerResultForFoodUnitTotal() {
		val pr = new PlayerResult();
		pr.addServedFoodServed("A", "a", 10);
		pr.addServedFoodServed("A", "a", 5);
		pr.addServedFoodServed("A", "a", 15);
		pr.addServedFoodServed("A", "b", 7);
		pr.addServedFoodServed("A", "b", 103);
		pr.addServedFoodServed("B", "a", 9);
		return pr;
	}

	@Test
	public void getServedFoodUnitsTotalTest() {
		val pr = createPlayerResultForFoodUnitTotal();
		Assert.assertEquals(6, pr.getServedFoodUnitsTotal());
	}

	@Test
	public void getServedFoodEstablishmentUnitsTotalTest() {
		val pr = createPlayerResultForFoodUnitTotal();
		Assert.assertEquals(6, pr.getServedFoodEstablishmentUnitsTotal());
	}

	@Test
	public void getServedFoodRevenueTotalTest() {
		val pr = createPlayerResultForFoodUnitTotal();
		Assert.assertEquals(149, pr.getServedFoodRevenueTotal(), 0d);
	}

	@Test
	public void getServedFoodEstablishmentRevenueTotalTest() {
		val pr = createPlayerResultForFoodUnitTotal();
		Assert.assertEquals(149, pr.getServedFoodEstablishmentRevenueTotal(), 0d);
	}

	@Test
	public void addServedFoodServedTest() {
		val pr = new PlayerResult();
		pr.addServedFoodServed("A", "a", 10);
		pr.addServedFoodServed("A", "a", 5);
		pr.addServedFoodServed("A", "a", 15);
		pr.addServedFoodServed("A", "b", 7);
		pr.addServedFoodServed("A", "b", 103);
		pr.addServedFoodServed("B", "a", 9);
		Assert.assertEquals(5, (long) pr.getServedFoodPerEstablishmentUnits().get("A"));
		Assert.assertEquals(4, (long) pr.getServedFoodPerTypeUnits().get("a"));
		Assert.assertEquals(140, (double) pr.getServedFoodPerEstablishmentRevenue().get("A"), 0d);
		Assert.assertEquals(39, (double) pr.getServedFoodPerTypeRevenue().get("a"), 0d);
	}

	@Test
	public void addGuestsOutOfIngPerCityTest() {
		val pr = new PlayerResult();
		pr.addGuestsOutOfIngPerCity("A");
		pr.addGuestsOutOfIngPerCity("A");
		pr.addGuestsOutOfIngPerCity("A");
		pr.addGuestsOutOfIngPerCity("B");
		Assert.assertEquals(3, (long) pr.getGuestsOutOfIngPerCity().get("A"));
		Assert.assertEquals(1, (long) pr.getGuestsOutOfIngPerCity().get("B"));
	}

	@Test
	public void addMissingIngredientsTest() {
		val pr = new PlayerResult();
		val missingIngredients1 = new ArrayList<>();
		missingIngredients1.add("A");
		missingIngredients1.add("B");
		missingIngredients1.add("C");
		pr.addMissingIngredients(missingIngredients1);
		val missingIngredients2 = new ArrayList<>();
		missingIngredients2.add("A");
		missingIngredients2.add("B");
		pr.addMissingIngredients(missingIngredients2);
		val missingIngredients3 = new ArrayList<>();
		missingIngredients3.add("A");
		pr.addMissingIngredients(missingIngredients3);
		Assert.assertEquals(3, (long) pr.getMissingIngredients().get("A"));
		Assert.assertEquals(2, (long) pr.getMissingIngredients().get("B"));
		Assert.assertEquals(1, (long) pr.getMissingIngredients().get("C"));
	}

	@Test
	public void addTotalBribeTest() {
		val pr = new PlayerResult();
		pr.addTotalBribe(5);
		pr.addTotalBribe(7);
		pr.addTotalBribe(9);
		Assert.assertEquals(21, pr.getTotalBribe());
	}

	@Test
	public void addTotalInteriorTest() {
		val pr = new PlayerResult();
		pr.addTotalInterior(5);
		pr.addTotalInterior(7);
		pr.addTotalInterior(9);
		Assert.assertEquals(21, pr.getTotalInterior());
	}

	@Test
	public void addDebugTest() {
		val pr = new PlayerResult();
		pr.addDebug("this is a test");
		pr.addDebug("something different");
		Assert.assertEquals("this is a test<br/>something different<br/>", pr.getDebug().toString());
	}

	@Test
	public void addMenuEntryScoreTest() {
		val pr = new PlayerResult();
		pr.addMenuEntryScore("A", 10);
		pr.addMenuEntryScore("A", 20);
		pr.addMenuEntryScore("A", 30);
		pr.addMenuEntryScore("B", 20);
		Assert.assertEquals(20d, pr.getMenuEntryScore().get("A").average(), 0d);
		Assert.assertEquals(20d, pr.getMenuEntryScore().get("B").average(), 0d);
	}

	@Test
	public void addEstablishmentScoreTest() {
		val pr = new PlayerResult();
		pr.addEstablishmentScore("A", 10);
		pr.addEstablishmentScore("A", 20);
		pr.addEstablishmentScore("A", 30);
		pr.addEstablishmentScore("B", 20);
		Assert.assertEquals(20d, pr.getEstablishmentScore().get("A").average(), 0d);
		Assert.assertEquals(20d, pr.getEstablishmentScore().get("B").average(), 0d);
	}

}
