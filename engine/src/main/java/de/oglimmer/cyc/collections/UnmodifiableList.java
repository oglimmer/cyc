package de.oglimmer.cyc.collections;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class UnmodifiableList<E> extends UnmodifiableCollection<E> implements JavaScriptList<E> {
	private static final long serialVersionUID = 1L;
	final List<? extends E> list;

	UnmodifiableList(List<? extends E> list) {
		super(list);
		this.list = list;
	}

	public boolean equals(Object o) {
		return o == this || list.equals(o);
	}

	public int hashCode() {
		return list.hashCode();
	}

	public E get(int index) {
		try {
			return list.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	public E remove(int index) {
		throw new UnsupportedOperationException();
	}

	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	public ListIterator<E> listIterator(int index) {
		return new _ListIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return new UnmodifiableList<>(list.subList(fromIndex, toIndex));
	}

	@Override
	public void each(ForEach r) {
		for (E e : this) {
			r.run(e);
		}
	}

	class _ListIterator implements ListIterator<E> {
		private final ListIterator<? extends E> i;

		_ListIterator(int index) {
			i = list.listIterator(index);
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public E next() {
			return i.next();
		}

		public boolean hasPrevious() {
			return i.hasPrevious();
		}

		public E previous() {
			return i.previous();
		}

		public int nextIndex() {
			return i.nextIndex();
		}

		public int previousIndex() {
			return i.previousIndex();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void set(E e) {
			throw new UnsupportedOperationException();
		}

		public void add(E e) {
			throw new UnsupportedOperationException();
		}
	}
}