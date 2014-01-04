package de.oglimmer.cyc.api;

public interface IEstablishmentRule {

	int getScore(Object establishment);

	double getScore(Object establishment, int type);

	boolean checkMinRequirements(Object establishment);
}
