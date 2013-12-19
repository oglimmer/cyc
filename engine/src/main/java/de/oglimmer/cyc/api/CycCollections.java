package de.oglimmer.cyc.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class CycCollections {

	public static <T> JavaScriptList<T> unmodifiableList(List<? extends T> c) {
		return new UnmodifiableList<>(c);
	}

	public static <T> JavaScriptSet<T> unmodifiableSet(Set<? extends T> s) {
		return new UnmodifiableSet<>(s);
	}

	static class UnmodifiableSet<E> extends UnmodifiableCollection<E> implements JavaScriptSet<E>, Serializable {
		private static final long serialVersionUID = -9215047833775013803L;

		UnmodifiableSet(Set<? extends E> s) {
			super(s);
		}

		public boolean equals(Object o) {
			return o == this || c.equals(o);
		}

		public int hashCode() {
			return c.hashCode();
		}

		@Override
		public void each(ForEach r) {
			for (E e : this) {
				r.run(e);
			}
		}

	}

	static class UnmodifiableCollection<E> implements Collection<E>, Serializable {
		private static final long serialVersionUID = 1L;

		final Collection<? extends E> c;

		UnmodifiableCollection(Collection<? extends E> c) {
			if (c == null)
				throw new NullPointerException();
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

	static class UnmodifiableList<E> extends UnmodifiableCollection<E> implements JavaScriptList<E> {
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

		public ListIterator<E> listIterator(final int index) {
			return new ListIterator<E>() {
				private final ListIterator<? extends E> i = list.listIterator(index);

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
			};
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
	}

}
