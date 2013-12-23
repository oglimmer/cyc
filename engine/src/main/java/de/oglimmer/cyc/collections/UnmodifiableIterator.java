package de.oglimmer.cyc.collections;

import java.util.Iterator;

public class UnmodifiableIterator<T> implements Iterator<T> {

	private Iterator<T> iterator;

	public UnmodifiableIterator(Iterator<T> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public T next() {
		return iterator.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}