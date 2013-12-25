package de.oglimmer.cyc.util;

import java.util.HashMap;

public class CountDoubleMap<K> extends HashMap<K, Double> {

	private static final long serialVersionUID = 1L;

	public synchronized void add(K key, double value) {
		Double currentVal = get(key);
		if (currentVal == null) {
			put(key, value);
		} else {
			currentVal += value;
			put(key, currentVal);
		}
	}

}
