package de.oglimmer.cyc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

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

	public Collection<K> max(int pos) {
		return new MaxCalculator().max(pos);
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

	class MaxCalculator {

		public Collection<K> max(int pos) {

			Map<K, LongMutable> sorted = getSortedMap();

			Map<Long, Collection<K>> sortedStacked = new StackedTransformator(sorted).getSortedStacked();

			if (sortedStacked.size() <= pos) {
				return Collections.emptyList();
			}

			removeFirstItems(pos, sortedStacked);

			return sortedStacked.values().iterator().next();
		}

		private void removeFirstItems(int pos, Map<Long, Collection<K>> sortedStacked) {
			for (int i = 0; i < pos; i++) {
				sortedStacked.remove(sortedStacked.keySet().iterator().next());
			}
		}

		private Map<K, LongMutable> getSortedMap() {
			Map<K, LongMutable> sortedMap = new LinkedHashMap<>();
			entrySet().stream().sorted(Comparator.comparing(e -> -1 * e.getValue().val))
					.forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));
			return sortedMap;
		}

		class StackedTransformator {
			@Getter
			private Map<Long, Collection<K>> sortedStacked = new LinkedHashMap<>();
			private long lastKey = -1;
			private Collection<K> lastValueCol = null;

			private StackedTransformator(Map<K, LongMutable> sorted) {
				for (Map.Entry<K, LongMutable> en : sorted.entrySet()) {
					processEntry(en);
				}
				putLastCollectedIntoResult();
			}

			private void processEntry(Map.Entry<K, LongMutable> en) {
				K oldKeyNewVal = en.getKey();
				long oldValNewKey = en.getValue().val;
				if (lastKey != oldValNewKey) {
					putLastCollectedIntoResult();
					lastValueCol = new ArrayList<>();
					lastKey = oldValNewKey;
				}
				lastValueCol.add(oldKeyNewVal);
			}

			private void putLastCollectedIntoResult() {
				if (lastValueCol != null) {
					sortedStacked.put(lastKey, lastValueCol);
				}
			}
		}
	}

}
