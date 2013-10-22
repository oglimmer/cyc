package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FoodDelivery implements Container<FoodUnit> {

	private List<FoodUnit> list = new ArrayList<>();

	void add(FoodUnit fu) {
		list.add(fu);
	}

	public Iterator<FoodUnit> iterator() {
		return new UnmodifiableIterator<FoodUnit>(list.iterator());
	}

	public void each(ForEach r) {
		for (FoodUnit ap : list) {
			r.run(ap);
		}
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public FoodUnit get(int index) {
		return list.get(index);
	}

	@Override
	public String toString() {
		return Arrays.toString(list.toArray());
	}
}
