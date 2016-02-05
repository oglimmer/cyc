package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyStatisticsManager {

	private Map<Company, DailyStatistics> lastDayStatsPerCompany;

	private Phase phase = new SingleThreadPhase(null);

	void startMultiThreading() {
		assert phase instanceof SingleThreadPhase;
		phase.flip();
	}

	void endMultiThreading() {
		assert phase instanceof ThreadSafePhase;
		phase.flip();
	}

	DailyStatistics getLastDays(Company company) {
		DailyStatistics ds = lastDayStatsPerCompany.get(company);
		if (ds == null) {
			ds = new DailyStatistics();
		}
		return ds;
	}

	DailyStatistics getCollecting(Company company) {
		Map<Company, DailyStatistics> collectingMap = phase.getStats();
		DailyStatistics ds = collectingMap.get(company);
		if (ds == null) {
			ds = new DailyStatistics();
			collectingMap.put(company, ds);
		}
		return ds;
	}

	interface Phase {
		Map<Company, DailyStatistics> getStats();

		void flip();
	}

	class ThreadSafePhase implements Phase {

		private java.lang.ThreadLocal<Map<Company, DailyStatistics>> statsPerCompanyPerThread = new java.lang.ThreadLocal<>();
		private List<Map<Company, DailyStatistics>> statsPerCompanyTotalList = new ArrayList<>();

		@Override
		public Map<Company, DailyStatistics> getStats() {
			Map<Company, DailyStatistics> threadLocalStatsPerComp = statsPerCompanyPerThread.get();
			if (threadLocalStatsPerComp == null) {
				threadLocalStatsPerComp = new HashMap<Company, DailyStatistics>();
				statsPerCompanyTotalList.add(threadLocalStatsPerComp);
				statsPerCompanyPerThread.set(threadLocalStatsPerComp);
			}
			return threadLocalStatsPerComp;
		}

		@Override
		public void flip() {
			phase = new SingleThreadPhase(statsPerCompanyTotalList);
		}
	}

	class SingleThreadPhase implements Phase {
		private Map<Company, DailyStatistics> statsPerCompany = new HashMap<>();

		SingleThreadPhase(List<Map<Company, DailyStatistics>> statsPerCompanyList) {
			if (statsPerCompanyList != null) {
				for (Map<Company, DailyStatistics> collectedMaps : statsPerCompanyList) {
					DailyStatistics.add(statsPerCompany, collectedMaps);
				}
			}
		}

		@Override
		public Map<Company, DailyStatistics> getStats() {
			return statsPerCompany;
		}

		@Override
		public void flip() {
			lastDayStatsPerCompany = getStats();
			phase = new ThreadSafePhase();
		}
	}

}
