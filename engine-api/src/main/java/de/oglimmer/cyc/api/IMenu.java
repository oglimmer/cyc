package de.oglimmer.cyc.api;

import java.util.Collection;

public interface IMenu {

	int size();

	IMenuEntry get(int index);

	Collection<IMenuEntry> getIMenuEntries();

}
