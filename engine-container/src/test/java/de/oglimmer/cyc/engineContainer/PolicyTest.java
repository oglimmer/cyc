package de.oglimmer.cyc.engineContainer;

import java.io.FilePermission;
import java.security.Policy;
import java.util.PropertyPermission;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class PolicyTest {

	private CycPolicy pol;

	@Before
	public void warmUp() {
		System.setProperty("cyc.home", ".");
		pol = new CycPolicy(Policy.getPolicy());
	}

	@Test
	public void first() {
		FilePermission permission = new FilePermission(".", "read");
		Assert.assertTrue(pol.implies(null, permission));
	}

	@Test
	public void f2() {
		FilePermission permission = new FilePermission("./logs/foo.log", "read");
		Assert.assertTrue(pol.implies(null, permission));
	}

	@Test
	public void f3() {
		FilePermission permission = new FilePermission("./logs/foo.log", "read,write");
		Assert.assertTrue(pol.implies(null, permission));
	}

	@Test
	public void f4() {
		FilePermission permission = new FilePermission("./logs/foo.log", "write,read");
		Assert.assertTrue(pol.implies(null, permission));
	}

	@Test
	public void f5() {
		FilePermission permission = new FilePermission("./logs/foo.log", "write,read,delete");
		Assert.assertFalse(pol.implies(null, permission));
	}

	@Test
	public void f6() {
		FilePermission permission = new FilePermission("/groovy/script", "read");
		Assert.assertTrue(pol.implies(null, permission));
	}

	@Test
	public void f7() {
		FilePermission permission = new FilePermission(
				"/Users/oli/dev/java/workspace/cyc/engine-container/../../../ifcdb/bin/", "read");
		Assert.assertFalse(pol.implies(null, permission));
	}

	@Test
	public void f8() {
		FilePermission permission = new FilePermission(
				"/Users/oli/dev/java/workspace/cyc/engine-container/engine-container-jar-with-dependencies.jar", "read");
		Assert.assertTrue(pol.implies(null, permission));
	}

	@Test
	public void propertyTest() {
		PropertyPermission permission = new PropertyPermission("org.apache.commons.logging.Log", "read");
		Assert.assertTrue(pol.implies(null, permission));
	}

	@Test
	public void reduce1() {
		Assert.assertEquals("/Users/oli/dev/java/workspace/cyc/engine-container", pol.reduce(".", false));
	}

	@Test
	public void reduce2() {
		Assert.assertEquals("/Users/oli/dev/java/workspace/cyc/engine-container",
				pol.reduce("/Users/oli/dev/java/workspace/cyc/engine-container/.", false));
	}

	@Test
	public void reduce3() {
		Assert.assertEquals("/Users/oli/dev/java/workspace/cyc/engine-container",
				pol.reduce("/Users/oli/dev/java/workspace/cyc/engine-container/././.", false));
	}

	@Test
	public void reduce4() {
		Assert.assertEquals("/Users/oli/dev/java/workspace/cyc/engine-container",
				pol.reduce("/Users/oli/dev/java/workspace/./cyc/engine-container/.", false));
	}

	@Test
	public void reduce5() {
		Assert.assertEquals("/Users/oli/dev/java/workspace/cyc/engine-container", pol.reduce("././", false));
	}

	@Test
	public void reduce6() {
		Assert.assertEquals("/Users/oli/dev/java/workspace",
				pol.reduce("/Users/oli/dev/java/workspace/cyc/engine-container/../..", false));
	}

	@Test
	public void reduce7() {
		Assert.assertEquals("/Users/oli/java/workspace/engine-container",
				pol.reduce("/Users/oli/dev/../java/workspace/engine-container/cyc/..", false));
	}
}
