package de.oglimmer.cyc;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import de.oglimmer.cyc.api.Constants;
import de.oglimmer.cyc.api.Game;
import de.oglimmer.cyc.api.GroovyInitializer;
import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.engine.IGameRunStarter;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameRunStarter implements IGameRunStarter {

	@Override
	public void startFullGame() {
		GroovyInitializer.globalInit();
		log.debug("Running full game");
		List<User> userList = DataProviderCouchDB.INSTANCE.allPlayers();

		if (!userList.isEmpty()) {
			Game game = new Game(Constants.Mode.FULL, DataProviderCouchDB.INSTANCE);
			game.executeGame(userList);
		}

		inactivateUsers(DataProviderCouchDB.INSTANCE);
	}

	@Override
	public void startCheckRun(String uid) {
		assert uid != null && !uid.isEmpty();
		GroovyInitializer.globalInit();

		List<User> userList = DataProviderCouchDB.INSTANCE.singlePlayer(uid);

		if (!userList.isEmpty()) {
			Game game = new Game(Constants.Mode.SINGLE, DataProviderCouchDB.INSTANCE);
			GameRun gameRun = game.executeGame(userList);

			DataProviderCouchDB.INSTANCE.writeTestRunProtocol(uid, gameRun, game);
		}
	}

	@Override
	public void startTestRun(String numberOfUsers) {
		assert numberOfUsers != null && !numberOfUsers.isEmpty();
		GroovyInitializer.globalInit();
		IDataProvider dataProvider = new DataProviderMemory(numberOfUsers);
		List<User> userList = dataProvider.allPlayers();

		if (!userList.isEmpty()) {
			Game game = new Game(Constants.Mode.FULL, dataProvider);
			game.executeGame(userList);
		}

		inactivateUsers(dataProvider);
	}

	@Override
	public String getVersion() {
		String commit = "?";
		String version = "?";
		String creationDate = "?";

		Class<?> clazz = this.getClass();
		String className = clazz.getSimpleName() + ".class";
		String classPath = clazz.getResource(className).toString();
		if (classPath.startsWith("jar")) {
			try {
				String manifestPath = classPath.substring(0, classPath.lastIndexOf('!') + 1) + "/META-INF/MANIFEST.MF";
				Manifest manifest = new Manifest(new URL(manifestPath).openStream());
				Attributes attr = manifest.getMainAttributes();
				commit = attr.getValue("SVN-Revision-No");
				version = attr.getValue("CYC-Version");
				long time = Long.parseLong(attr.getValue("Creation-Date"));
				creationDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
						.format(new Date(time));
			} catch (IOException e) {
				log.error("Failed to get engine version", e);
			}
		}

		return "V" + version + " [Commit#" + commit + "] build " + creationDate;
	}

	private void inactivateUsers(IDataProvider dataProvider) {
		Map<String, Integer> playersToRemove = findBankruptPlayers();
		dataProvider.setPlayersInactive(playersToRemove);
	}

	private Map<String, Integer> findBankruptPlayers() {
		Map<String, Integer> playersToRemove = new HashMap<>();
		GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());
		List<GameRun> runHistory = dao.findAllGameRun(IDataProvider.ROUNDS_TO_BE_EXCLUDED);
		for (GameRun gr : runHistory) {
			for (String parti : gr.getParticipants()) {
				Double total = gr.getResult().getPlayerResults().get(parti).getTotalAssets();
				if (total < 0) {
					if (playersToRemove.containsKey(parti)) {
						playersToRemove.put(parti, playersToRemove.get(parti) + 1);
					} else {
						playersToRemove.put(parti, 1);
					}
				}
			}
		}
		return playersToRemove;
	}

}
