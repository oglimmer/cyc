package de.oglimmer.cyc;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.cyc.api.Game;
import de.oglimmer.cyc.api.GroovyInitializer;
import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;

public class GameRunStarter {

	private static Logger log = LoggerFactory.getLogger(GameRunStarter.class);

	private static final int ROUNDS_TO_BE_EXCLUDED = 10;

	private int year, month, day;
	private boolean writeGameResult;
	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	public void startFullGame() {
		GroovyInitializer.globalInit();
		log.debug("Running full game");
		List<String[]> userList = allPlayers();

		if (!userList.isEmpty()) {
			Game game = new Game(year, month, day);
			game.executeGame(userList, writeGameResult);
		}

		inactivateUsers();

	}

	public void startCheckRun(String uid) {
		assert uid != null && !uid.isEmpty();
		GroovyInitializer.globalInit();
		List<String[]> userList = singlePlayer(uid);

		if (!userList.isEmpty()) {
			Game game = new Game(year, month, day);
			GameRun gameRun = game.executeGame(userList, writeGameResult);

			String lastError = gameRun.getResult().getError().toString();

			User u = userDao.get(uid);
			lastError = gameRun.getResult().get(u.getUsername()).getDebug().toString()
					+ (lastError.isEmpty() ? "Your script ran successfully." : lastError);
			u.setLastError(lastError);
			u.setLastPrivateRun(new Date());
			userDao.update(u);
		}
	}

	public String getVersion() {
		String commit = "?";
		String version = "?";
		String creationDate = "?";

		Class<?> clazz = this.getClass();
		String className = clazz.getSimpleName() + ".class";
		String classPath = clazz.getResource(className).toString();
		if (classPath.startsWith("jar")) {
			try {
				String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
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

	private List<String[]> allPlayers() {
		List<String[]> userList = new ArrayList<>();
		writeGameResult = true;

		List<User> ul = userDao.findAllUser();
		for (User u : ul) {
			if (u.isActive()) {
				userList.add(new String[] { u.getUsername(), u.getMainJavaScript() });
				log.debug("Adding player to game:" + u.getUsername());
			}
		}

		day = 30;
		month = 12;
		year = 1;
		return userList;
	}

	private void inactivateUsers() {

		Map<String, Integer> playersToRemove = findBankruptPlayers();

		List<User> ul = userDao.findAllUser();
		for (User u : ul) {
			if (playersToRemove.containsKey(u.getUsername())
					&& playersToRemove.get(u.getUsername()) == ROUNDS_TO_BE_EXCLUDED) {
				u.setActive(false);
				userDao.update(u);
			}
		}

	}

	private Map<String, Integer> findBankruptPlayers() {
		Map<String, Integer> playersToRemove = new HashMap<>();
		GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());
		List<GameRun> runHistory = dao.findAllGameRun(ROUNDS_TO_BE_EXCLUDED);
		for (GameRun gr : runHistory) {
			for (String parti : gr.getParticipants()) {
				Long total = gr.getResult().getPlayerResults().get(parti).getTotalAssets();
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

	private List<String[]> singlePlayer(String uid) {
		List<String[]> userList = new ArrayList<>();
		writeGameResult = false;
		log.debug("Running game for user:" + uid);
		User u = userDao.get(uid);
		userList.add(new String[] { u.getUsername(), u.getMainJavaScript() });
		log.debug("Adding player to game:" + u.getUsername());
		day = 10;
		month = 6;
		year = 1;
		return userList;
	}

	// private void singlePlayer(String[] args) {
	// writeGameResult = false;
	// if (args[0].equals("-file")) {
	// for (int i = 1; i < args.length; i++) {
	// try {
	// String script = new String(Files.readAllBytes(Paths.get(args[i])));
	// userList.add(new String[] { "Player_" + i, script });
	// } catch (IOException e) {
	// log.error("Failed to load script from filesystem", e);
	// }
	// }
	// } else {
	// log.debug("Running game for user:" + args[0]);
	// User u = userDao.get(args[0]);
	// userList.add(new String[] { u.getUsername(), u.getMainJavaScript() });
	// log.debug("Adding player to game:" + u.getUsername());
	// }
	// day = 10;
	// month = 6;
	// year = 1;
	// }
}
