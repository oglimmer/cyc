package de.oglimmer.cyc.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public class UnmodifiableCollection<E> implements Collection<E>, Serializable {
	private static final long serialVersionUID = 1L;

	final Collection<? extends E> c;

	UnmodifiableCollection(Collection<? extends E> c) {
		if (c == null) {
			throw new NullPointerException();
		}
		this.c = c;
	}

	public int size() {
		return c.size();
	}

	public boolean isEmpty() {
		return c.isEmpty();
	}

	public boolean contains(Object o) {
		return c.contains(o);
	}

	public Object[] toArray() {
		return c.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return c.toArray(a);
	}

	public String toString() {
		return c.toString();
	}

	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private final Iterator<? extends E> i = c.iterator();

			public boolean hasNext() {
				return i.hasNext();
			}

			public E next() {
				return i.next();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public boolean add(E e) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection<?> coll) {
		return c.containsAll(coll);
	}

	public boolean addAll(Collection<? extends E> coll) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> coll) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> coll) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}
}