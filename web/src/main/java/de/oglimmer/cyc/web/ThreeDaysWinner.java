package de.oglimmer.cyc.web;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
		Date[] startEndKey = getStartEndKey();
		List<GameWinners> listGameWinners = dao.findAllGameWinners(startEndKey[0], startEndKey[1]);
		return calcThreeDayWinner(listGameWinners, startEndKey);
	}

	public Date[] getStartEndKey() {
		Date[] returnDate = new Date[2];
		Date maxDate = WebContainerProperties.INSTANCE.getSystemHaltDate();
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime maxZDT = ZonedDateTime.ofInstant(maxDate.toInstant(), ZoneOffset.UTC);
		if (now.isAfter(maxZDT)) {
			ZonedDateTime startKeyDate = maxZDT.minusSeconds(1);
			ZonedDateTime endKeyDate = maxZDT.minusMinutes(4320);
			returnDate[0] = Date.from(startKeyDate.toInstant());
			returnDate[1] = Date.from(endKeyDate.toInstant());
		} else {
			ZonedDateTime endKeyDate = now.minusMinutes(4320);
			returnDate[1] = Date.from(endKeyDate.toInstant());
		}
		return returnDate;
	}

	Result calcThreeDayWinner(List<GameWinners> listGameWinners, Date[] startEndKey) {
		Result threeDayWinner;
		if (listGameWinners.isEmpty()) {
			threeDayWinner = new Result(new String[] { "-", "-", "-" }, "-", dateToString(startEndKey));
		} else {
			String[] threeDayWinnerS = calcLast3DaysWinner(listGameWinners);
			String lastWinner = calcLastWinner(listGameWinners);
			threeDayWinner = new Result(threeDayWinnerS, lastWinner, dateToString(startEndKey));
		}
		return threeDayWinner;
	}

	public String dateToString(Date[] startEndKey) {
		DateFormat df = DateFormat.getDateTimeInstance();
		if (startEndKey[0] != null) {
			return "from " + df.format(startEndKey[1]) + " to " + df.format(startEndKey[0]);
		} else {
			return "since " + df.format(startEndKey[1]);
		}
	}

	private String[] calcLast3DaysWinner(List<GameWinners> listGameWinners) {

		AverageMap<String> winAvgTotal = new AverageMap<>();

		aggregate(listGameWinners, winAvgTotal);
		return calcCountWinner(winAvgTotal);
	}

	private void aggregate(List<GameWinners> listGameWinners, AverageMap<String> winAvgTotal) {
		for (GameWinners gw : listGameWinners) {
			if (gw.getWinnerTotal() > 0) {
				winAvgTotal.add(gw.getWinnerName(), (long) gw.getWinnerTotal());
			}
		}
	}

	private String[] calcCountWinner(AverageMap<String> winAvgTotal) {
		CountMap<String> winCount = winAvgTotal.getCount();
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
			threeDayWinnerBuff.append(name + " (" + winAvgTotal.get(name).getNum() + "x)");
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
		private String threeDayWinnerTimeRange;
	}

}
