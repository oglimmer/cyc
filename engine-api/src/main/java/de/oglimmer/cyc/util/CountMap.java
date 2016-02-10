package de.oglimmer.cyc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CountMap<K> extends ConcurrentHashMap<K, LongMutable> {

	private static final long serialVersionUID = 1L;

	private static final LongMutable DEFAULT = new LongMutable();

	public long getLong(K key) {
		return getOrDefault(key, DEFAULT).val;
	}

	public void put(K key, long val) {
		put(key, new LongMutable(val));
	}

	public void add(K key, long value) {
		LongMutable currentVal = get(key);
		if (currentVal == null) {
			put(key, new LongMutable(value));
		} else {
			currentVal.val += value;
		}
	}

	public void add(K key, LongMutable value) {
		LongMutable currentVal = get(key);
		if (currentVal == null) {
			put(key, value);
		} else {
			currentVal.val += value.val;
		}
	}

	public void sub(K key, long value) {
		LongMutable currentVal = get(key);
		if (currentVal == null) {
			assert false;
		} else {
			currentVal.val -= value;
			assert currentVal.val >= 0;
			put(key, currentVal);
		}
	}

	public Collection<K> max() {
		Collection<K> listWinners = new ArrayList<>();
		long maxCount = -1;
		for (K key : keySet()) {
			LongMutable wins = get(key);
			if (wins.val > maxCount) {
				listWinners.clear();
				listWinners.add(key);
				maxCount = wins.val;
			} else if (wins.val == maxCount) {
				listWinners.add(key);
			}
		}
		return listWinners;
	}

	public long sum() {
		long sum = 0;
		for (LongMutable l : values()) {
			sum += l.val;
		}
		return sum;
	}

	public void addAll(Map<K, LongMutable> toAdd) {
		for (Map.Entry<K, LongMutable> en : toAdd.entrySet()) {
			this.add(en.getKey(), en.getValue());
		}
	}

}
