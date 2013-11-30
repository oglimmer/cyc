package de.oglimmer.cyc.engineContainer;


public class DebugEngineLoader extends EngineLoader {

	protected void init() {
		// no code here
	}

	protected Object createGameRunStarter() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Class<?> clazz = Class.forName("de.oglimmer.cyc.GameRunStarter");
		return clazz.newInstance();
	}

}
