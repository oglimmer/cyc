package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Menu implements Container<MenuEntry>, Iterable<MenuEntry> {

	private List<MenuEntry> entries = new ArrayList<>();

	public void add(String name, String[] ingredients, int price) {
		entries.add(new MenuEntry(name, ingredients, price));
	}

	public void remove(String name) {
		for (Iterator<MenuEntry> it = entries.iterator(); it.hasNext();) {
			MenuEntry me = it.next();
			if (me.getName().equals(name)) {
				it.remove();
			}
		}
	}

	@Override
	public int size() {
		return entries.size();
	}

	@Override
	public void each(ForEach r) {
		for (Object o : entries) {
			r.run(o);
		}
	}

	@Override
	public Iterator<MenuEntry> iterator() {
		return new UnmodifiableIterator<>(entries.iterator());
	}

	@Override
	public MenuEntry get(int index) {
		return entries.get(index);
	}

	@Override
	public String toString() {
		return "Menu [entries=" + Arrays.toString(entries.toArray()) + "]";
	}

}
