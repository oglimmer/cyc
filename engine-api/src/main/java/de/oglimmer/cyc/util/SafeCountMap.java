package de.oglimmer.cyc.util;

import java.util.HashMap;

public class SafeCountMap<K> extends HashMap<K, Long> {

	private static final long serialVersionUID = 1L;

	public SafeCountMap(CountMap<K> initialMap) {
		initialMap.entrySet().forEach(en -> put(en.getKey(), en.getValue().val));
	}

	@Override
	public Long get(Object key) {
		Long lm = super.get(key);
		if (lm == null) {
			return 0L;
		}
		return lm;
	}

}
