package de.oglimmer.cyc.api;

import java.util.Iterator;

public interface Container<T> {

	int size();

	void each(ForEach r);

	Iterator<T> iterator();

	T get(int index);

}
