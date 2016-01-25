package de.oglimmer.cyc.engineContainer;

import de.oglimmer.cyc.engine.IGameRunStarter;
import lombok.SneakyThrows;

public class DebugEngineLoader extends EngineLoader {

	protected void init() {
		// no code here
	}

	@SneakyThrows(value = { ClassNotFoundException.class, IllegalAccessException.class, InstantiationException.class })
	protected IGameRunStarter createGameRunStarter() {
		Class<? extends IGameRunStarter> clazz = Class.forName("de.oglimmer.cyc.GameRunStarter")
				.asSubclass(IGameRunStarter.class);
		return clazz.newInstance();
	}

}
