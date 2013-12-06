package de.oglimmer.cyc.engineContainer;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

public class CLTest {

	@Ignore
	@Test
	public void unloadClassTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException,
			ClassNotFoundException, InterruptedException {

		EngineLoader el = new EngineLoader() {
			protected void init() {
			}
		};
		el.baseDir = "";
		for (int i = 1; i < 10; i++) {
			System.out.println("----:" + i);
			el.currentDir = "/usr/local/cyr-engine-container/cyc0" + (i < 10 ? "0" + i : i);
			el.initClassLoader();
			el.startGame(null);
			System.gc();
			TimeUnit.SECONDS.sleep(3);
		}

		el.currentDir = "/usr/local/cyr-engine-container/cyc000";
		el.initClassLoader();
		System.gc();
		TimeUnit.SECONDS.sleep(900);

	}

}
