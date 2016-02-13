package de.oglimmer.cyc.web;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.model.GameWinners;
import de.oglimmer.cyc.web.ThreeDaysWinner.Result;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CouchDbUtil.class)
public class ThreeDaysWinnerTest {

	@BeforeClass
	public static void prepare() {
		PowerMockito.mockStatic(CouchDbUtil.class);
		Mockito.when(CouchDbUtil.getDatabase()).thenReturn(null);
	}

	@Test
	public void testEmpty() {
		List<GameWinners> listGameWinners = new ArrayList<>();

		Result result = ThreeDaysWinner.INSTANCE.calcThreeDayWinner(listGameWinners);
		Assert.assertEquals("-", result.getThreeDayWinner()[0]);
		Assert.assertEquals("-", result.getThreeDayWinner()[1]);
		Assert.assertEquals("-", result.getThreeDayWinner()[2]);
		Assert.assertEquals("-", result.getLastWinner());
	}

	@Test
	public void testAllBankrupt1() {
		List<GameWinners> listGameWinners = new ArrayList<>();

		listGameWinners.add(createGameWinners("a", -1));
		listGameWinners.add(createGameWinners("a", -1));
		listGameWinners.add(createGameWinners("b", -1));

		Result result = ThreeDaysWinner.INSTANCE.calcThreeDayWinner(listGameWinners);
		Assert.assertEquals("a (bankrupt)", result.getThreeDayWinner()[0]);
		Assert.assertEquals("b (bankrupt)", result.getThreeDayWinner()[1]);
		Assert.assertEquals("-", result.getThreeDayWinner()[2]);
		Assert.assertEquals("a (bankrupt)", result.getLastWinner());
	}

	@Test
	public void testAllBankrupt2() {
		List<GameWinners> listGameWinners = new ArrayList<>();

		listGameWinners.add(createGameWinners("a", -1));

		Result result = ThreeDaysWinner.INSTANCE.calcThreeDayWinner(listGameWinners);
		Assert.assertEquals("a (bankrupt)", result.getThreeDayWinner()[0]);
		Assert.assertEquals("-", result.getThreeDayWinner()[1]);
		Assert.assertEquals("-", result.getThreeDayWinner()[2]);
		Assert.assertEquals("a (bankrupt)", result.getLastWinner());
	}

	@Test
	public void testAllBankrupt3() {
		List<GameWinners> listGameWinners = new ArrayList<>();

		listGameWinners.add(createGameWinners("a", -1));
		listGameWinners.add(createGameWinners("b", -1));

		Result result = ThreeDaysWinner.INSTANCE.calcThreeDayWinner(listGameWinners);
		Assert.assertTrue(result.getThreeDayWinner()[0].contains("b (bankrupt)"));
		Assert.assertTrue(result.getThreeDayWinner()[0].contains("a (bankrupt)"));
		Assert.assertEquals("-", result.getThreeDayWinner()[1]);
		Assert.assertEquals("-", result.getThreeDayWinner()[2]);
		Assert.assertEquals("a (bankrupt)", result.getLastWinner());
	}

	@Test
	public void test3PlayersMiddle() {
		List<GameWinners> listGameWinners = new ArrayList<>();

		listGameWinners.add(createGameWinners("b", 100));
		listGameWinners.add(createGameWinners("b", 200));
		listGameWinners.add(createGameWinners("a", 10));
		listGameWinners.add(createGameWinners("a", 20));
		listGameWinners.add(createGameWinners("a", 30));
		listGameWinners.add(createGameWinners("c", 1000));
		listGameWinners.add(createGameWinners("c", 200));

		Result result = ThreeDaysWinner.INSTANCE.calcThreeDayWinner(listGameWinners);
		Assert.assertEquals("a ($20.00)", result.getThreeDayWinner()[0]);
		Assert.assertTrue(result.getThreeDayWinner()[1].contains("b ($150.00)"));
		Assert.assertTrue(result.getThreeDayWinner()[1].contains("c ($600.00)"));
		Assert.assertTrue(result.getThreeDayWinner()[2].equals("-"));
		Assert.assertEquals("b ($100.00)", result.getLastWinner());
	}

	@Test
	public void test3PlayersEnd() {
		List<GameWinners> listGameWinners = new ArrayList<>();

		listGameWinners.add(createGameWinners("a", 10));
		listGameWinners.add(createGameWinners("b", 100));
		listGameWinners.add(createGameWinners("b", 200));
		listGameWinners.add(createGameWinners("a", 20));
		listGameWinners.add(createGameWinners("a", 30));
		listGameWinners.add(createGameWinners("c", 1000));
		listGameWinners.add(createGameWinners("c", 200));

		Result result = ThreeDaysWinner.INSTANCE.calcThreeDayWinner(listGameWinners);
		Assert.assertEquals("a ($20.00)", result.getThreeDayWinner()[0]);
		Assert.assertTrue(result.getThreeDayWinner()[1].contains("b ($150.00)"));
		Assert.assertTrue(result.getThreeDayWinner()[1].contains("c ($600.00)"));
		Assert.assertTrue(result.getThreeDayWinner()[2].equals("-"));
		Assert.assertEquals("a ($10.00)", result.getLastWinner());
	}

	@Test
	public void test3PlayersTie() {
		List<GameWinners> listGameWinners = new ArrayList<>();

		listGameWinners.add(createGameWinners("b", 100));
		listGameWinners.add(createGameWinners("a", 10));
		listGameWinners.add(createGameWinners("a", 20));
		listGameWinners.add(createGameWinners("a", 30));
		listGameWinners.add(createGameWinners("c", 1000));
		listGameWinners.add(createGameWinners("c", 2000));
		listGameWinners.add(createGameWinners("c", 3000));

		Result result = ThreeDaysWinner.INSTANCE.calcThreeDayWinner(listGameWinners);
		Assert.assertTrue(result.getThreeDayWinner()[0].contains("c ($2,000.00)"));
		Assert.assertTrue(result.getThreeDayWinner()[0].contains("a ($20.00)"));
		Assert.assertTrue(result.getThreeDayWinner()[1].equals("b ($100.00)"));
		Assert.assertTrue(result.getThreeDayWinner()[2].equals("-"));
		Assert.assertEquals("b ($100.00)", result.getLastWinner());
	}

	private GameWinners createGameWinners(final String name, final double total) {
		return new GameWinners() {
			@Override
			public String getWinnerName() {
				return name;
			}

			@Override
			public double getWinnerTotal() {
				return total;
			}
		};
	}
}
