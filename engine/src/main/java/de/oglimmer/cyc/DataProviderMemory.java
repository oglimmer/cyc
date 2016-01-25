package de.oglimmer.cyc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.oglimmer.cyc.api.Game;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.util.DefaultCode;

public class DataProviderMemory implements IDataProvider {

	public static final DataProviderMemory INSTANCE = new DataProviderMemory();

	private List<User> users = new ArrayList<>();

	public DataProviderMemory() {
		this("1");
	}

	public DataProviderMemory(String numberOfUser) {
		for (int i = 0; i < Integer.parseInt(numberOfUser); i++) {
			User user = new User();
			user.setActive(true);
			user.setCreatedDate(new Date());
			user.setEmail("no" + i + "@no.de");
			user.setId("id:" + i);
			user.setMainJavaScript(DefaultCode.INSTANCE.getDefaultCode());
			user.setUsername("player " + i);
			users.add(user);
		}
	}

	@Override
	public List<User> allPlayers() {
		return users;
	}

	@Override
	public List<User> singlePlayer(String uid) {
		return users;
	}

	@Override
	public void writeTestRunProtocol(String uid, GameRun gameRun, Game game) {
	}

	@Override
	public void setPlayersInactive(Map<String, Integer> playersToRemove) {
	}

	@Override
	public void writeGameResults(GameRun gameRun) {
	}

}
