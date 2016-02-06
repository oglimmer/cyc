package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Not thread-safe.
 * 
 * @author oli
 *
 */
@Slf4j
public class DailyStatisticsManagerCityProcessor {

	@Getter
	private Map<Company, DailyStatistics> collecting = new HashMap<Company, DailyStatistics>();

	DailyStatistics getCollecting(Company company) {

		if (!Thread.currentThread().getName().startsWith("CityProcessor")) {
			assert false;
			log.error("FAILED TO CHECK THREAD-NAME. THIS RUNS IN AN UNEXPECTED THREAD", new RuntimeException());
		}

		DailyStatistics ds = collecting.get(company);
		if (ds == null) {
			ds = new DailyStatistics();
			collecting.put(company, ds);
		}
		return ds;
	}

}
