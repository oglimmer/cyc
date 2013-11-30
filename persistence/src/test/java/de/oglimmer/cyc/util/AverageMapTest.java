package de.oglimmer.cyc.util;

import org.junit.Assert;
import org.junit.Test;

public class AverageMapTest {

	@Test
	public void simpleAverage() {
		AverageMap<Integer> map = new AverageMap<>();
		map.add(1, 1);
		map.add(1, 2);
		map.add(1, 3);
		Assert.assertEquals(2d, map.get(1).average(), 0d);
	}

	@Test
	public void multiAverage() {
		AverageMap<Integer> map = new AverageMap<>();
		map.add(1, 1);
		map.add(1, 2);
		map.add(1, 3);
		map.add(2, 10);
		map.add(2, 20);
		map.add(2, 30);
		map.add(3, 0);
		map.add(3, 0);
		map.add(3, 0);
		Assert.assertEquals(2d, map.get(1).average(), 0d);
		Assert.assertEquals(20d, map.get(2).average(), 0d);
		Assert.assertEquals(0d, map.get(3).average(), 0d);
	}
}
