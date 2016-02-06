package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Most methods are NOT thread-safe!
 * 
 * @author oli
 *
 */
@Slf4j
public class DailyStatisticsManager {

	private Map<Company, DailyStatistics> currentDay = new HashMap<Company, DailyStatistics>();
	private Map<Company, DailyStatistics> lastDay = new HashMap<Company, DailyStatistics>();

	void reset() {
		lastDay = currentDay;
		currentDay = new HashMap<Company, DailyStatistics>();
	}

	DailyStatistics getLastDay(Company company) {
		DailyStatistics ds = lastDay.get(company);
		if (ds == null) {
			ds = new DailyStatistics();
		}
		return ds;
	}

	DailyStatistics getCurrentDay(Company company) {

		if (!Thread.currentThread().getName().startsWith("TestRun")
				&& !Thread.currentThread().getName().startsWith("FullRun")) {
			assert false;
			log.error("FAILED TO CHECK THREAD-NAME. THIS RUNS IN AN UNEXPECTED THREAD", new RuntimeException());
		}

		DailyStatistics ds = currentDay.get(company);
		if (ds == null) {
			ds = new DailyStatistics();
			currentDay.put(company, ds);
		}
		return ds;
	}

	public synchronized void add(DailyStatisticsManagerCityProcessor toAdd) {
		DailyStatistics.add(currentDay, toAdd.getCollecting());
	}
}
