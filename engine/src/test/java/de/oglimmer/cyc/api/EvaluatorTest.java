package de.oglimmer.cyc.api;

import net.sourceforge.jeval.EvaluationException;

import org.junit.Assert;
import org.junit.Test;

import de.oglimmer.cyc.api.Grocer.BulkOrderDiscount;

public class EvaluatorTest {

	@Test
	public void foodPriceChangeTest() throws EvaluationException {
		for (int i = 0; i < 5000; i++) {
			double b = i / 100;
			double np = Constants.INSTACE.getFoodPriceChange(b);
			Assert.assertTrue("np=" + np, np >= b * 0.98 && np <= b * 1.02);
		}
	}

	@Test
	public void bulkOrderDiscountsTest() throws EvaluationException {
		BulkOrderDiscount[] bods = Constants.INSTACE.getBulkOrderDiscounts();
		int prevAmount = -1;
		double prevDiscount = 1;
		for (BulkOrderDiscount bod : bods) {
			Assert.assertTrue(prevAmount < bod.getStartingAmount());
			Assert.assertTrue(prevDiscount > bod.getDiscount());
			prevAmount = bod.getStartingAmount();
			prevDiscount = bod.getDiscount();
		}
	}

	@Test
	public void rangeTestNumberApplicationProfilesChef() {
		int NO_COMP = 10;
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = Constants.INSTACE.getNumberApplicationProfiles("chef", NO_COMP);
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
			int val = Constants.INSTACE.getNumberApplicationProfiles("waiter", NO_COMP);
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
			int val = Constants.INSTACE.getNumberApplicationProfiles("manager", NO_COMP);
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
			int val = Constants.INSTACE.getQualification();
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
			int val = Constants.INSTACE.getSalary(1);
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
			int val = Constants.INSTACE.getSalary(10);
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
			int val = Constants.INSTACE.getSalePrice(1, 25);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 0);
		Assert.assertTrue(max <= 20000);
		System.out.println("rangeTestSalePriceLow=" + min + " -> " + max);
	}

	@Test
	public void rangeTestSalePriceHigh() {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = Constants.INSTACE.getSalePrice(10, 249);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 0);
		Assert.assertTrue(max <= 2_000_000);
		System.out.println("rangeTestSalePriceHigh=" + min + " -> " + max);
	}

	@Test
	public void rangeTestLeaseCostsLow() {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = Constants.INSTACE.getLeaseCosts(1, 25);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 25);
		Assert.assertTrue(max <= 224);
		System.out.println("rangeTestLeaseCostsLow=" + min + " -> " + max);
	}

	@Test
	public void rangeTestLeaseCostsHigh() {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < 1000; i++) {
			int val = Constants.INSTACE.getLeaseCosts(10, 249);
			if (min > val) {
				min = val;
			}
			if (max < val) {
				max = val;
			}
		}
		Assert.assertTrue(min >= 250);
		Assert.assertTrue(max <= 20_250);
		System.out.println("rangeTestLeaseCostsHigh=" + min + " -> " + max);
	}
}
