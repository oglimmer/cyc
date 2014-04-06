package de.oglimmer.cyc.util;

import java.util.HashMap;

abstract public class SafeMap<K, V> extends HashMap<K, V> {

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

}
