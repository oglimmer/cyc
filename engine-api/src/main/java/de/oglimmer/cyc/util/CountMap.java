package de.oglimmer.cyc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
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

			TreeMap<K, LongMutable> sorted = getSortedMap();

			TreeMap<Long, Collection<K>> sortedStacked = new StackedTransformator(sorted).getSortedStacked();

			if (sortedStacked.size() <= pos) {
				return Collections.emptyList();
			}

			removeFirstItems(pos, sortedStacked);

			return sortedStacked.firstEntry().getValue();
		}

		private void removeFirstItems(int pos, TreeMap<Long, Collection<K>> sortedStacked) {
			for (int i = 0; i < pos; i++) {
				sortedStacked.remove(sortedStacked.firstKey());
			}
		}

		private TreeMap<K, LongMutable> getSortedMap() {
			TreeMap<K, LongMutable> sortedMap = new TreeMap<>(new Comparator<K>() {
				@Override
				public int compare(K o1, K o2) {
					return -1 * Long.compare(get(o1).val, get(o2).val);
				}
			});
			sortedMap.putAll(CountMap.this);
			return sortedMap;
		}

		class StackedTransformator {
			@Getter
			private TreeMap<Long, Collection<K>> sortedStacked = new TreeMap<>();
			private long lastValue = -1;
			private Collection<K> lastCol = null;

			private StackedTransformator(TreeMap<K, LongMutable> sorted) {
				for (Map.Entry<K, LongMutable> en : sorted.entrySet()) {
					processEntry(en);
				}
				putLastCollectedIntoResult();
			}

			private void processEntry(Map.Entry<K, LongMutable> en) {
				if (lastValue != en.getValue().val) {
					putLastCollectedIntoResult();
					lastCol = new ArrayList<>();
					lastValue = en.getValue().val;
				}
				lastCol.add(en.getKey());
			}

			private void putLastCollectedIntoResult() {
				if (lastCol != null) {
					sortedStacked.put(lastValue, lastCol);
				}
			}
		}
	}

}
