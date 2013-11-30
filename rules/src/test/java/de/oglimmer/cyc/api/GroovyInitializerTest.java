package de.oglimmer.cyc.api;

import org.junit.Assert;
import org.junit.Test;

public class GroovyInitializerTest {

	@Test
	public void loadGroovyScriptTest() {
		GroovyInitializer<GITest> gi = new GroovyInitializer<>();
		gi.loadGroovyScript("de.oglimmer.cyc.api.TestRule.groovy");
		GITest testInter = gi.getGroovyObject();
		Assert.assertTrue(testInter instanceof GITest);
		Assert.assertEquals(1, testInter.test());
	}

	public interface GITest {
		int test();
	}
}
