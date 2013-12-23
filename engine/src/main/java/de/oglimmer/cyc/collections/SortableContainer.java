package de.oglimmer.cyc.collections;

public interface SortableContainer<T> extends Container<T> {

	T getLowest();

	T getHighest();

}
