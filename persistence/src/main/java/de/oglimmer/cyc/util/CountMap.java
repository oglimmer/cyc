package de.oglimmer.cyc.util;

import java.util.HashMap;

public class CountMap<K> extends HashMap<K, Long> {

	private static final long serialVersionUID = 1L;

	public void add(K key, long value) {
		Long currentVal = get(key);
		if (currentVal == null) {
			put(key, value);
		} else {
			currentVal += value;
			put(key, currentVal);
		}
	}

}
