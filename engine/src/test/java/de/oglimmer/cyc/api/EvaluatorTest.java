package de.oglimmer.cyc.api;

import net.sourceforge.jeval.EvaluationException;

import org.junit.Assert;
import org.junit.Test;

import de.oglimmer.cyc.api.Grocer.BulkOrderDiscount;

public class EvaluatorTest {

	private Constants constants = new Constants(Constants.Mode.FULL);

	@Test
	public void foodPriceChangeTest() throws EvaluationException {
		for (int i = 0; i < 5000; i++) {
			double b = i / 100;
			double np = constants.getFoodPriceChange(b);
			Assert.assertTrue("np=" + np, np >= b * 0.98 && np <= b * 1.02);
		}
	}

	@Test
	public void bulkOrderDiscountsTest() throws EvaluationException {
		BulkOrderDiscount[] bods = constants.getBulkOrderDiscounts();
		int prevAmount = Integer.MAX_VALUE;
		double prevDiscount = 0;
		for (BulkOrderDiscount bod : bods) {
			Assert.assertTrue(prevAmount > bod.getStartingAmount());
			Assert.assertTrue(prevDiscount < bod.getDiscount());
			prevAmount = bod.getStartingAmount();
			prevDiscount = bod.getDiscount();
		}
	}

	@Test
	public void rangeTestNumberApplicationProfilesChef() {
		int NO_COMP = 10;
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = constants.getNumberApplicationProfiles("chef", NO_COMP);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= NO_COMP);
		Assert.assertTrue(max < 3 * NO_COMP);
		System.out.println("rangeTestNumberApplicationProfilesChef=" + min + " -> " + max);
	}

	@Test
	public void rangeTestNumberApplicationProfilesWaiter() {
		int NO_COMP = 10;
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = constants.getNumberApplicationProfiles("waiter", NO_COMP);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= NO_COMP);
		Assert.assertTrue(max < 3 * NO_COMP);
		System.out.println("rangeTestNumberApplicationProfilesWaiter=" + min + " -> " + max);
	}

	@Test
	public void rangeTestNumberApplicationProfilesManager() {
		int NO_COMP = 10;
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = constants.getNumberApplicationProfiles("manager", NO_COMP);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 0);
		Assert.assertTrue(max < NO_COMP);
		System.out.println("rangeTestNumberApplicationProfilesManager=" + min + " -> " + max);
	}

	@Test
	public void rangeTestQualification() {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = constants.getQualification();
			if (min > val) {
				min = val;

			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 1);
		Assert.assertTrue(max <= 10);
		System.out.println("rangeTestQualification=" + min + " -> " + max);
	}

	@Test
	public void rangeTestSalaryLow() {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = constants.getSalary(1);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 200);
		Assert.assertTrue(max < 1200);
		System.out.println("rangeTestSalary=" + min + " -> " + max);
	}

	@Test
	public void rangeTestSalaryHigh() {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = constants.getSalary(10);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 2000);
		Assert.assertTrue(max < 12000);
		System.out.println("rangeTestSalary=" + min + " -> " + max);
	}

	@Test
	public void rangeTestSalePriceLow() {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = constants.getSalePrice(1, 25);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 1250);
		Assert.assertTrue(max <= 2499);
		System.out.println("rangeTestSalePriceLow=" + min + " -> " + max);
	}

	@Test
	public void rangeTestSalePriceHigh() {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = constants.getSalePrice(10, 250);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 125000);
		Assert.assertTrue(max <= 250000);
		System.out.println("rangeTestSalePriceHigh=" + min + " -> " + max);
	}

	@Test
	public void rangeTestLeaseCostsLow() {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = constants.getLeaseCosts(1, 25);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue("min<" + min, min >= 250);
		Assert.assertTrue("max>" + max, max <= 374);
		System.out.println("rangeTestLeaseCostsLow=" + min + " -> " + max);
	}

	@Test
	public void rangeTestLeaseCostsHigh() {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = constants.getLeaseCosts(10, 250);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 25000);
		Assert.assertTrue(max <= 37500);
		System.out.println("rangeTestLeaseCostsHigh=" + min + " -> " + max);
	}
}
