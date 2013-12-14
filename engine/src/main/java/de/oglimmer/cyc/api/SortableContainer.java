package de.oglimmer.cyc.api;

public interface SortableContainer<T> extends Container<T> {

	T getLowest();

	T getHighest();

}
