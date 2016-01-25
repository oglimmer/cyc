package de.oglimmer.cyc;

import java.util.List;
import java.util.Map;

import de.oglimmer.cyc.api.Game;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;

public interface IDataProvider {

	int ROUNDS_TO_BE_EXCLUDED = 10;

	List<User> allPlayers();

	List<User> singlePlayer(String uid);

	void writeTestRunProtocol(String uid, GameRun gameRun, Game game);

	void setPlayersInactive(Map<String, Integer> playersToRemove);

	void writeGameResults(GameRun gameRun);

}
