package de.oglimmer.cyc.api;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class GameResultTest {

	@Test
	public void winner() {
		GameResult gr = new GameResult();
		PlayerResult prA = gr.getCreateNotExists("a");
		prA.setTotalAssets(100);
		PlayerResult prB = gr.getCreateNotExists("b");
		prB.setTotalAssets(200);
		PlayerResult prC = gr.getCreateNotExists("c");
		prC.setTotalAssets(-1);
		Assert.assertEquals("b ($200.00)", gr.getWinner());
	}

	@Test
	public void foodChart() {
		GameResult gr = new GameResult();
		PlayerResult prA = gr.getCreateNotExists("a");
		prA.addMenuEntryScore("A", 7);
		prA.addMenuEntryScore("A", 10);
		prA.addMenuEntryScore("A", 13);
		prA.addMenuEntryScore("B", 1);
		prA.addMenuEntryScore("B", 5);
		prA.addMenuEntryScore("B", 9);
		Map<String, Double> foodChart = gr.getFoodChart();
		Assert.assertEquals(1d, (double) foodChart.get("A (a)"), 0d);
		Assert.assertEquals(.5d, (double) foodChart.get("B (a)"), 0d);
	}

	@Test
	public void establishmentChart() {
		GameResult gr = new GameResult();
		PlayerResult prA = gr.getCreateNotExists("a");
		prA.addEstablishmentScore("A", 7);
		prA.addEstablishmentScore("A", 10);
		prA.addEstablishmentScore("A", 13);
		prA.addEstablishmentScore("B", 1);
		prA.addEstablishmentScore("B", 5);
		prA.addEstablishmentScore("B", 9);
		Map<String, Double> estChart = gr.getEstablishmentChart();
		Assert.assertEquals(1d, (double) estChart.get("A (a)"), 0d);
		Assert.assertEquals(.5d, (double) estChart.get("B (a)"), 0d);
	}

}
