package de.oglimmer.cyc.web;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import lombok.Value;

import org.ektorp.CouchDbConnector;

import de.oglimmer.cyc.dao.GameWinnersDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameWinnersCouchDb;
import de.oglimmer.cyc.model.GameWinners;
import de.oglimmer.cyc.util.AverageMap;
import de.oglimmer.cyc.util.CountMap;

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
			threeDayWinner = new Result("-", "-");
		} else {
			String lastWinner = calcLastWinner(listGameWinners);
			String threeDayWinnerS = calcLast3DaysWinner(listGameWinners);
			threeDayWinner = new Result(threeDayWinnerS, lastWinner);
		}
		return threeDayWinner;
	}

	private String calcLast3DaysWinner(List<GameWinners> listGameWinners) {

		AverageMap<String> winAvgTotal = new AverageMap<>();
		CountMap<String> winCount = new CountMap<>();

		aggregate(listGameWinners, winAvgTotal, winCount);
		return calcCountWinner(winAvgTotal, winCount);
	}

	private void aggregate(List<GameWinners> listGameWinners, AverageMap<String> winAvgTotal, CountMap<String> winCount) {
		for (GameWinners gw : listGameWinners) {
			winCount.add(gw.getWinnerName(), 1);
			winAvgTotal.add(gw.getWinnerName(), (long) gw.getWinnerTotal());
		}
	}

	private String calcCountWinner(AverageMap<String> winAvgTotal, CountMap<String> winCount) {
		StringBuilder threeDayWinnerBuff = new StringBuilder(64);
		for (String name : winCount.max()) {
			if (threeDayWinnerBuff.length() > 0) {
				threeDayWinnerBuff.append(", ");
			}
			threeDayWinnerBuff.append(name + " (" + formatTotal(winAvgTotal.get(name).average()) + ")");
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
		private String threeDayWinner;
		private String lastWinner;
	}

}
