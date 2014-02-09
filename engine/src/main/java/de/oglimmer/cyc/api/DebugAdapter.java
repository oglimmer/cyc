package de.oglimmer.cyc.api;

import lombok.Value;

@Value
public class DebugAdapter {

	private Game game;
	private GameResult gameResult;
	private String name;

	public void println(String str) {
		gameResult.get(name).addDebug(str);
	}

	public void log(String str) {
		println(str);
	}

	public void setDayStatisticDescription(int type, String description) {
		gameResult.get(name).getStatistics().setCustomStatisticsName(type, description);
	}

	public void setDayStatistic(int type, Number value) {
		if (value == null) {
			value = 0D;
		}
		gameResult.get(name).getStatistics().setCustomStatistics(game.getCurrentDay(), type, value.doubleValue());
	}
}
