package de.oglimmer.cyc.web.winner;

import java.text.NumberFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.ektorp.CouchDbConnector;

import de.oglimmer.cyc.dao.GameWinnersDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameWinnersCouchDb;
import de.oglimmer.cyc.model.GameWinners;
import de.oglimmer.cyc.util.AverageMap;
import de.oglimmer.cyc.util.CountMap;
import de.oglimmer.cyc.web.WebContainerProperties;
import lombok.AllArgsConstructor;

public enum WinnerHistoryCalculation {
	INSTANCE;

	private static final int WINNER_CALC_TIMESPAN = 4320; // 3 days in minutes
	private static final int NUMBER_OF_PLACES_TO_CALC = 3;

	private GameWinnersDao dao;

	private WinnerHistoryCalculation() {
		CouchDbConnector database = CouchDbUtil.getDatabase();
		if (database != null) {
			dao = new GameWinnersCouchDb(database);
		}
	}

	public WinnerResult calc() {
		WinnerStartEndDate startEndKey = createStartEndKey();
		List<GameWinners> listGameWinners = dao.findAllGameWinners(startEndKey.getStart(), startEndKey.getEnd());
		return calc(listGameWinners, startEndKey);
	}

	private WinnerStartEndDate createStartEndKey() {
		WinnerStartEndDate startEndDate = new WinnerStartEndDate();
		Date maxDate = WebContainerProperties.INSTANCE.getSystemHaltDate();
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime maxZDT = ZonedDateTime.ofInstant(maxDate.toInstant(), ZoneOffset.UTC);
		if (now.isAfter(maxZDT)) {
			ZonedDateTime startKeyDate = maxZDT.minusSeconds(1);
			ZonedDateTime endKeyDate = maxZDT.minusMinutes(WINNER_CALC_TIMESPAN);
			startEndDate.setStart(Date.from(startKeyDate.toInstant()));
			startEndDate.setEnd(Date.from(endKeyDate.toInstant()));
		} else {
			ZonedDateTime endKeyDate = now.minusMinutes(WINNER_CALC_TIMESPAN);
			startEndDate.setEnd(Date.from(endKeyDate.toInstant()));
		}
		return startEndDate;
	}

	WinnerResult calc(List<GameWinners> listGameWinners, WinnerStartEndDate startEndKey) {
		String[] lastDayWinners = new ListOfWinners(listGameWinners).calc();
		String lastWinner = new LastWinner(listGameWinners).calc();
		return new WinnerResult(lastDayWinners, lastWinner, startEndKey, listGameWinners);
	}

	@AllArgsConstructor
	class ListOfWinners {

		private List<GameWinners> listGameWinners;

		private String[] calc() {
			if (listGameWinners.isEmpty()) {
				return calcNoWinners();
			} else {
				AverageMap<String> winAvgTotal = new AverageMap<>();
				aggregate(listGameWinners, winAvgTotal);
				return buildStringArray(winAvgTotal);
			}
		}

		private void aggregate(List<GameWinners> listGameWinners, AverageMap<String> winAvgTotal) {
			for (GameWinners gw : listGameWinners) {
				if (gw.getWinnerTotal() > 0) {
					winAvgTotal.add(gw.getWinnerName(), (long) gw.getWinnerTotal());
				}
			}
		}

		private String[] calcNoWinners() {
			List<String> resultList = new ArrayList<>(NUMBER_OF_PLACES_TO_CALC);
			for (int i = 0; i < NUMBER_OF_PLACES_TO_CALC; i++) {
				resultList.add("-");
			}
			return resultList.toArray(new String[NUMBER_OF_PLACES_TO_CALC]);
		}

		private String[] buildStringArray(AverageMap<String> winAvgTotal) {
			CountMap<String> winCount = winAvgTotal.getCount();
			List<String> resultList = new ArrayList<>(NUMBER_OF_PLACES_TO_CALC);
			for (int i = 0; i < NUMBER_OF_PLACES_TO_CALC; i++) {
				resultList.add(buildString(i, winAvgTotal, winCount));
			}
			return resultList.toArray(new String[NUMBER_OF_PLACES_TO_CALC]);
		}

		private String buildString(int pos, AverageMap<String> winAvgTotal, CountMap<String> winCount) {
			StringBuilder threeDayWinnerBuff = new StringBuilder(64);
			for (String name : winCount.max(pos)) {
				if (threeDayWinnerBuff.length() > 0) {
					threeDayWinnerBuff.append(", ");
				}
				threeDayWinnerBuff.append(name + " (" + winAvgTotal.get(name).getNum() + "x)");
			}
			if (threeDayWinnerBuff.length() == 0) {
				threeDayWinnerBuff.append("-");
			}
			return threeDayWinnerBuff.toString();
		}

	}

	@AllArgsConstructor
	class LastWinner {

		private List<GameWinners> listGameWinners;

		private String calc() {
			if (listGameWinners.isEmpty()) {
				return "-";
			} else {
				GameWinners lastGameWinner = listGameWinners.get(0);
				return lastGameWinner.getWinnerName() + " (" + formatTotal(lastGameWinner.getWinnerTotal()) + ")";
			}
		}

		private String formatTotal(double total) {
			if (total >= 0) {
				NumberFormat currencyDf = NumberFormat.getCurrencyInstance(Locale.US);
				return currencyDf.format(total);
			}
			return "bankrupt";
		}

	}

}
