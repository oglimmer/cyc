package de.oglimmer.cyc.api;

import lombok.Value;

@Value
public class DebugAdapter {

	private GameResult gameResult;
	private String name;

	public void println(String str) {
		gameResult.get(name).addDebug(str);
	}

	public void log(String str) {
		println(str);
	}
}
