package de.oglimmer.cyc.mbean;

public interface GameRunStatsMBean {

	int getTotalUsers();

	int getActiveUsers();

	int getInActiveUsers();

	int getShowCodeUsers();

	int getCheckRuns();

	void incCheckRuns();

	int getLastFullRunTime();

}
