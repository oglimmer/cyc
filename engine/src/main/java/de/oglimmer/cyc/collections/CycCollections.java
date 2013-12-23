package de.oglimmer.cyc.collections;

import java.util.List;
import java.util.Set;

public class CycCollections {

	private CycCollections() {
		// no code here
	}

	public static <T> JavaScriptList<T> unmodifiableList(List<? extends T> c) {
		return new UnmodifiableList<>(c);
	}

	public static <T> JavaScriptSet<T> unmodifiableSet(Set<? extends T> s) {
		return new UnmodifiableSet<>(s);
	}

}
