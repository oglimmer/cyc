package de.oglimmer.cyc.util;

import java.util.HashMap;

public class CountMap<K> extends HashMap<K, Long> {

	private static final long serialVersionUID = 1L;

	public synchronized void add(K key, long value) {
		Long currentVal = get(key);
		if (currentVal == null) {
			put(key, value);
		} else {
			currentVal += value;
			put(key, currentVal);
		}
	}

	public synchronized void sub(K key, long value) {
		Long currentVal = get(key);
		if (currentVal == null) {
			assert false;
		} else {
			currentVal -= value;
			assert currentVal >= 0;
			put(key, currentVal);
		}
	}

}
