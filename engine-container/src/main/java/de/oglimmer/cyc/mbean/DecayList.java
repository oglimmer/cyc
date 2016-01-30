package de.oglimmer.cyc.mbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DecayList {

	private final List<Date> entries = new ArrayList<>();
	private TimeUnit timeUnit;
	private int units;

	public synchronized int size() {
		clean();
		return entries.size();
	}

	public synchronized void add(Date e) {
		entries.add(e);
		clean();
	}

	private void clean() {
		for (Iterator<Date> it = entries.iterator(); it.hasNext();) {
			Date e = it.next();
			if (e.getTime() < System.currentTimeMillis() - timeUnit.toMillis(units)) {
				it.remove();
			}
		}
	}

}
