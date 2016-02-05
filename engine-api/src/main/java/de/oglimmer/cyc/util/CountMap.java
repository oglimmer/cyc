package de.oglimmer.cyc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

	public Collection<K> max() {
		Collection<K> listWinners = new ArrayList<>();
		long maxCount = -1;
		for (K key : keySet()) {
			long wins = get(key);
			if (wins > maxCount) {
				listWinners.clear();
				listWinners.add(key);
				maxCount = wins;
			} else if (wins == maxCount) {
				listWinners.add(key);
			}
		}
		return listWinners;
	}

	public long sum() {
		long sum = 0;
		for (Long l : values()) {
			sum += l;
		}
		return sum;
	}

	public void addAll(Map<K, Long> toAdd) {
		for (Map.Entry<K, Long> en : toAdd.entrySet()) {
			this.add(en.getKey(), en.getValue());
		}
	}

}
