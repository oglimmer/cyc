package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;

public class DailyStatisticsManager {

	private Map<Company, DailyStatistics> collecting = new HashMap<>();
	private Map<Company, DailyStatistics> lastDays = new HashMap<>();

	void reset() {
		lastDays = collecting;
		collecting = new HashMap<>();
	}

	DailyStatistics getLastDays(Company company) {
		DailyStatistics ds = lastDays.get(company);
		if (ds == null) {
			ds = new DailyStatistics();
		}
		return ds;
	}

	DailyStatistics getCollecting(Company company) {
		DailyStatistics ds = collecting.get(company);
		if (ds == null) {
			ds = new DailyStatistics();
			collecting.put(company, ds);
		}
		return ds;
	}
}
