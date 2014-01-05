package de.oglimmer.cyc.api;

public interface IMenuEntryRule {

	int getDeliciousness(Object ingredients);

	int getDeliciousness(Object ingredients, int type);

}
