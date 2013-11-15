package de.oglimmer.cyc.api;

public class DebugAdapter {

	private GameResult gameResult;
	private String name;

	public DebugAdapter(GameResult gameResult, String name) {
		this.gameResult = gameResult;
		this.name = name;
	}

	public void println(String str) {
		gameResult.get(name).addDebug(str);
	}
}
