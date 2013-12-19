package de.oglimmer.cyc.api;

import org.junit.Assert;
import org.junit.Test;

import de.oglimmer.cyc.api.Constants.Mode;

public class GrocerTest {

	@Test
	public void testPrice1() {
		Game game = new Game(Mode.FULL);
		Grocer g = game.getGrocer();
		Assert.assertEquals(Food.BEEF_MEAT.getBasePrice(), g.getPrice(Food.BEEF_MEAT.toString(), 1), 0d);
	}

	@Test
	public void testPrice100() {
		Game game = new Game(Mode.FULL);
		Grocer g = game.getGrocer();
		Assert.assertEquals(100 * Food.BEEF_MEAT.getBasePrice() * 0.9, g.getPrice(Food.BEEF_MEAT.toString(), 100), 0d);
	}

	@Test
	public void testPrice5000() {
		Game game = new Game(Mode.FULL);
		Grocer g = game.getGrocer();
		Assert.assertEquals(5000 * Food.BEEF_MEAT.getBasePrice() * 0.4, g.getPrice(Food.BEEF_MEAT.toString(), 5000), 0d);
	}

}
