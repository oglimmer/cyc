package de.oglimmer.cyc.api;

public class DebugAdapter {

	private GameResult gameResult;
	private boolean writeGameResult;

	public DebugAdapter(GameResult gameResult, boolean writeGameResult) {
		this.gameResult = gameResult;
		this.writeGameResult = writeGameResult;
	}

	public void println(String str) {
		if (!writeGameResult) {
			gameResult.addDebug(str);
		}
	}
}
