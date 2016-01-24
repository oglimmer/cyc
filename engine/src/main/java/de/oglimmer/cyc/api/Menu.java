package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.oglimmer.cyc.collections.Container;
import de.oglimmer.cyc.collections.ForEach;
import de.oglimmer.cyc.collections.UnmodifiableIterator;
import de.oglimmer.cyc.util.PublicAPI;
import de.oglimmer.cyc.util.UndocumentedAPI;

public class Menu implements Container<MenuEntry>, Iterable<MenuEntry>, IMenu {

	private List<MenuEntry> entries = new ArrayList<>();

	private Game game;

	Menu(Game game) {
		this.game = game;
	}

	@PublicAPI
	public void add(String name, String[] ingredients, double price) {
		entries.add(new MenuEntry(game, name, ingredients, price));
	}

	@PublicAPI
	public void remove(String name) {
		for (Iterator<MenuEntry> it = entries.iterator(); it.hasNext();) {
			MenuEntry me = it.next();
			if (me.getName().equals(name)) {
				it.remove();
			}
		}
	}

	@PublicAPI
	@Override
	public int size() {
		return entries.size();
	}

	@PublicAPI
	@Override
	public void each(ForEach r) {
		for (Object o : entries) {
			r.run(o);
		}
	}

	@PublicAPI
	@Override
	public Iterator<MenuEntry> iterator() {
		return new UnmodifiableIterator<>(entries.iterator());
	}

	@PublicAPI
	@Override
	public MenuEntry get(int index) {
		return entries.get(index);
	}
	
	@UndocumentedAPI
	@Override
	public Collection<IMenuEntry> getIMenuEntries() {
		return (Collection) Collections.unmodifiableList(entries);
	}

	@Override
	public String toString() {
		return "Menu [entries=" + Arrays.toString(entries.toArray()) + "]";
	}

}
