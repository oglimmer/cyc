package de.oglimmer.cyc.util;

import java.util.HashMap;
import java.util.Map;

abstract public class SafeMap<K, V extends CountMap<K>> extends HashMap<K, V> {

	private static final long serialVersionUID = 1L;

	protected abstract V createValue();

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		V v = super.get(key);
		if (v == null) {
			v = createValue();
			put((K) key, v);
		}
		return v;
	}

	public void addAll(Map<K, V> toAdd) {
		for (Map.Entry<K, V> en : toAdd.entrySet()) {
			V val = get(en.getKey());
			if (val == null) {
				put(en.getKey(), en.getValue());
			} else {
				val.addAll(en.getValue());
			}
		}
	}

}
