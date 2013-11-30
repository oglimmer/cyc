package de.oglimmer.cyc.util;

import org.junit.Assert;
import org.junit.Test;

public class CountMapTest {

	@Test
	public void simpleAverage() {
		CountMap<Integer> map = new CountMap<>();
		map.add(1, 1);
		map.add(1, 2);
		map.add(1, 3);
		Assert.assertEquals(6L, (long) map.get(1));
	}

	@Test
	public void multiAverage() {
		CountMap<Integer> map = new CountMap<>();
		map.add(1, 1);
		map.add(1, 2);
		map.add(1, 3);
		map.add(2, 10);
		map.add(2, 20);
		map.add(2, 30);
		map.add(3, 0);
		map.add(3, 0);
		map.add(3, 0);
		Assert.assertEquals(6L, (long) map.get(1));
		Assert.assertEquals(60L, (long) map.get(2));
		Assert.assertEquals(0L, (long) map.get(3));
	}
}
