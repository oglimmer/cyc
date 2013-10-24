package de.oglimmer.cyc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.oglimmer.cyc.api.Game;
import de.oglimmer.cyc.api.GroovyInitializer;
import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;

public class GameApp {

	public static final String VERSION = "1.0";

	public static void main(String[] args) throws Exception {

		System.setSecurityManager(new SecurityManager() {
			@Override
			public void checkExit(int status) {
				throw new RuntimeException("Exit not allowed");
			}
		});

		GroovyInitializer.init();

		new GameApp().startGame(args);
	}

	private int year, month, day;
	private boolean writeGameResult;
	private List<String[]> userList = new ArrayList<>();

	public GameApp() {

	}

	private void startGame(String[] args) throws IOException {

		fillUserList(args);

		if (!userList.isEmpty()) {
			Game gamePro = new Game(year, month, day);
			gamePro.executeGame(userList, writeGameResult);
		}
	}

	private void fillUserList(String[] args) throws IOException {
		UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());
		if (args.length < 1 || args[0].trim().isEmpty()) {
			allPlayers(userDao);
		} else {
			singlePlayer(args, userDao);
		}
	}

	private void allPlayers(UserDao userDao) {
		writeGameResult = true;

		Map<String, Integer> playersToRemove = findBankruptPlayers();

		List<User> ul = userDao.findAllUser();
		for (User u : ul) {
			if (playersToRemove.containsKey(u.getUsername()) && playersToRemove.get(u.getUsername()) == 3) {
				u.setActive(false);
				userDao.update(u);
			} else if (u.isActive()) {
				userList.add(new String[] { u.getUsername(), u.getMainJavaScript() });
			}
		}

		day = 30;
		month = 12;
		year = 1;
	}

	private Map<String, Integer> findBankruptPlayers() {
		Map<String, Integer> playersToRemove = new HashMap<>();
		GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());
		List<GameRun> runHistory = dao.findAllGameRun(3);
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

	private void singlePlayer(String[] args, UserDao userDao) throws IOException {
		writeGameResult = false;
		if (args[0].equals("-file")) {
			for (int i = 1; i < args.length; i++) {
				String script = new String(Files.readAllBytes(Paths.get(args[i])));
				userList.add(new String[] { "Player_" + i, script });
			}
		} else {
			User u = userDao.get(args[0]);
			userList.add(new String[] { u.getUsername(), u.getMainJavaScript() });
		}
		day = 10;
		month = 6;
		year = 1;
	}
}
