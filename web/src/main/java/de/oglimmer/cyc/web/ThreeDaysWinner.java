package de.oglimmer.cyc.web;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.ektorp.CouchDbConnector;

import de.oglimmer.cyc.dao.GameWinnersDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameWinnersCouchDb;
import de.oglimmer.cyc.model.GameWinners;
import de.oglimmer.cyc.util.AverageMap;
import de.oglimmer.cyc.util.CountMap;
import lombok.Value;

public enum ThreeDaysWinner {
	INSTANCE;

	private GameWinnersDao dao;

	private ThreeDaysWinner() {
		CouchDbConnector database = CouchDbUtil.getDatabase();
		if (database != null) {
			dao = new GameWinnersCouchDb(database);
		}
	}

	public Result calcThreeDayWinner() {
		List<GameWinners> listGameWinners = dao.findAllGameWinners(288);
		return calcThreeDayWinner(listGameWinners);
	}

	Result calcThreeDayWinner(List<GameWinners> listGameWinners) {
		Result threeDayWinner;
		if (listGameWinners.isEmpty()) {
			threeDayWinner = new Result(new String[] { "-", "-", "-" }, "-");
		} else {
			String[] threeDayWinnerS = calcLast3DaysWinner(listGameWinners);
			String lastWinner = calcLastWinner(listGameWinners);
			threeDayWinner = new Result(threeDayWinnerS, lastWinner);
		}
		return threeDayWinner;
	}

	private String[] calcLast3DaysWinner(List<GameWinners> listGameWinners) {

		AverageMap<String> winAvgTotal = new AverageMap<>();
		CountMap<String> winCount = new CountMap<>();

		aggregate(listGameWinners, winAvgTotal, winCount);
		return calcCountWinner(winAvgTotal, winCount);
	}

	private void aggregate(List<GameWinners> listGameWinners, AverageMap<String> winAvgTotal,
			CountMap<String> winCount) {
		for (GameWinners gw : listGameWinners) {
			winCount.add(gw.getWinnerName(), 1);
			winAvgTotal.add(gw.getWinnerName(), (long) gw.getWinnerTotal());
		}
	}

	private String[] calcCountWinner(AverageMap<String> winAvgTotal, CountMap<String> winCount) {
		String threeDayWinner0 = buildCountWinner(0, winAvgTotal, winCount);
		String threeDayWinner1 = buildCountWinner(1, winAvgTotal, winCount);
		String threeDayWinner2 = buildCountWinner(2, winAvgTotal, winCount);
		return new String[] { threeDayWinner0, threeDayWinner1, threeDayWinner2 };
	}

	private String buildCountWinner(int pos, AverageMap<String> winAvgTotal, CountMap<String> winCount) {
		StringBuilder threeDayWinnerBuff = new StringBuilder(64);
		for (String name : winCount.max(pos)) {
			if (threeDayWinnerBuff.length() > 0) {
				threeDayWinnerBuff.append(", ");
			}
			threeDayWinnerBuff.append(name + " (" + formatTotal(winAvgTotal.get(name).average()) + ")");
		}
		if (threeDayWinnerBuff.length() == 0) {
			threeDayWinnerBuff.append("-");
		}
		return threeDayWinnerBuff.toString();
	}

	private String calcLastWinner(List<GameWinners> listGameWinners) {
		GameWinners lastGameWinner = listGameWinners.get(0);
		return lastGameWinner.getWinnerName() + " (" + formatTotal(lastGameWinner.getWinnerTotal()) + ")";
	}

	private String formatTotal(double total) {
		NumberFormat currencyDf = NumberFormat.getCurrencyInstance(Locale.US);
		if (total >= 0) {
			return currencyDf.format(total);
		}
		return "bankrupt";
	}

	@Value
	public class Result {
		private String[] threeDayWinner;
		private String lastWinner;
	}

}
