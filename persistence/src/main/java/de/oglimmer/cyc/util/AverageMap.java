package de.oglimmer.cyc.util;

import java.util.HashMap;

public class AverageMap<K> extends HashMap<K, Average> {

	private static final long serialVersionUID = 1L;

	public synchronized void add(K key, long value) {
		Average currentVal = get(key);
		if (currentVal == null) {
			put(key, new Average(value));
		} else {
			currentVal.add(value);
		}
	}
	
	public CountMap<K> getCount() {
		CountMap<K> ret = new CountMap<K>();
		entrySet().stream().forEach(en -> ret.put(en.getKey(), en.getValue().getNum()));
		return ret;
	}

}
