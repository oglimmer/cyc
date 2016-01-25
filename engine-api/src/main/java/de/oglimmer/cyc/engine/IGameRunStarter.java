package de.oglimmer.cyc.engine;

public interface IGameRunStarter {

	String getVersion();

	void startFullGame();

	void startCheckRun(String clientRequest);

	void startTestRun(String numberOfUsers);

}
