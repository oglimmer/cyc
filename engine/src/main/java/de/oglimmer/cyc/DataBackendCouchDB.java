package de.oglimmer.cyc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.oglimmer.cyc.api.Game;
import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.GameWinnersDao;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.dao.couchdb.GameWinnersCouchDb;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.GameWinners;
import de.oglimmer.cyc.model.GameWinners.GameWinnerEntry;
import de.oglimmer.cyc.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataBackendCouchDB implements IDataBackend {

	public static final DataBackendCouchDB INSTANCE = new DataBackendCouchDB();

	private DataBackendCouchDB() {
	}

	private GameRunDao gameRunDao = new GameRunCouchDb(CouchDbUtil.getDatabase());
	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());
	private GameWinnersDao gameWinnersDao = new GameWinnersCouchDb(CouchDbUtil.getDatabase());

	public List<User> allPlayers() {
		List<User> userList = new ArrayList<>();

		List<User> ul = userDao.findAllUser();
		for (User u : ul) {
			if (u.isActive()) {
				userList.add(u);
				log.debug("Adding player to game:" + u.getUsername());
			}
		}

		return userList;
	}

	public List<User> singlePlayer(String uid) {
		List<User> userList = new ArrayList<>();

		log.debug("Running game for user:" + uid);
		User u = userDao.get(uid);
		userList.add(u);
		log.debug("Adding player to game:" + u.getUsername());
		return userList;
	}

	public void writeTestRunProtocol(String uid, GameRun gameRun, Game game) {
		String lastError = gameRun.getResult().getError().toString();
		User u = userDao.get(uid);
		lastError = gameRun.getResult().get(u.getUsername()).getDebug().toString()
				+ (lastError.isEmpty() ? "Your script ran successfully for " + game.getTotalMonth() + " months with "
						+ game.getTotalDay() + " days each." : lastError);
		u.setLastError(lastError);
		u.setLastPrivateRun(new Date());
		userDao.update(u);
	}

	public void setPlayersInactive(Map<String, Integer> playersToRemove) {
		List<User> ul = userDao.findAllUser();
		for (User u : ul) {
			if (playersToRemove.containsKey(u.getUsername())
					&& playersToRemove.get(u.getUsername()) == ROUNDS_TO_BE_EXCLUDED) {
				u.setActive(false);
				userDao.update(u);
			}
		}
	}

	public void writeGameResults(GameRun gameRun) {
		gameRunDao.add(gameRun);

		GameWinners gw = new GameWinners();
		gw.setRefGameRunId(gameRun.getId());
		gw.setStartTime(gameRun.getStartTime());
		for (String company : gameRun.getParticipants()) {
			gw.getParticipants().add(new GameWinnerEntry(company, gameRun.getResult().get(company).getTotalAssets()));
		}
		gameWinnersDao.add(gw);
	}

}
