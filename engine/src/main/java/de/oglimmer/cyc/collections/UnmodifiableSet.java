package de.oglimmer.cyc.collections;

import java.io.Serializable;
import java.util.Set;

public class UnmodifiableSet<E> extends UnmodifiableCollection<E> implements JavaScriptSet<E>, Serializable {
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