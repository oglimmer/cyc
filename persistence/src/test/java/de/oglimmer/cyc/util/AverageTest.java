package de.oglimmer.cyc.util;

import org.junit.Assert;
import org.junit.Test;

public class AverageTest {

	@Test
	public void testAverage() {
		Average a = new Average();
		a.add(5);
		a.add(7);
		a.add(9);
		Assert.assertEquals(7, a.average(), 0d);
	}

}
