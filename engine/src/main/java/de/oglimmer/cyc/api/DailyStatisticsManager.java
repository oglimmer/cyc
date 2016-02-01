package de.oglimmer.cyc.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DailyStatisticsManager {

	private Map<Company, DailyStatistics> collecting = Collections
			.synchronizedMap(new HashMap<Company, DailyStatistics>());
	private Map<Company, DailyStatistics> lastDays = Collections
			.synchronizedMap(new HashMap<Company, DailyStatistics>());

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
