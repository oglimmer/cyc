package de.oglimmer.cyc.mbean;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.engineContainer.GameServer;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;
import lombok.SneakyThrows;

public class GameRunStats implements GameRunStatsMBean {

	private GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());
	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	private enum CACHE_TYPE {
		TOTAL_USERS, ACTIVE_USERS, INACTIVE_USERS, LAST_FULLRUN_TIME, SHOW_CODE_USERS
	}

	private DecayList counterCheckRuns = new DecayList(TimeUnit.SECONDS, 15);

	private LoadingCache<CACHE_TYPE, Integer> totalUsersCache = CacheBuilder.newBuilder()
			.refreshAfterWrite(15, TimeUnit.SECONDS).build(new CacheLoader<CACHE_TYPE, Integer>() {
				public Integer load(CACHE_TYPE key) {
					switch (key) {
					case TOTAL_USERS:
						return userDao.findAllUser().size();
					case ACTIVE_USERS:
						int c = 0;
						for (User u : userDao.findAllUser()) {
							if (u.isActive()) {
								c++;
							}
						}
						return c;
					case INACTIVE_USERS:
						c = 0;
						for (User u : userDao.findAllUser()) {
							if (!u.isActive()) {
								c++;
							}
						}
						return c;
					case LAST_FULLRUN_TIME:
						List<GameRun> listGameRuns = dao.findAllGameRun(1);
						if (!listGameRuns.isEmpty()) {
							GameRun gr = listGameRuns.get(0);
							return (int) ((gr.getEndTime().getTime() - gr.getStartTime().getTime()) / 1000);
						}
						return -1;
					case SHOW_CODE_USERS:
						return userDao.findByOpenSource(null).size();
					default:
						return null;
					}
				}
			});

	@SneakyThrows(value = ExecutionException.class)
	@Override
	public int getTotalUsers() {
		return totalUsersCache.get(CACHE_TYPE.TOTAL_USERS);
	}

	@SneakyThrows(value = ExecutionException.class)
	@Override
	public int getActiveUsers() {
		return totalUsersCache.get(CACHE_TYPE.ACTIVE_USERS);
	}

	@SneakyThrows(value = ExecutionException.class)
	@Override
	public int getInActiveUsers() {
		return totalUsersCache.get(CACHE_TYPE.INACTIVE_USERS);
	}

	@SneakyThrows(value = ExecutionException.class)
	@Override
	public int getLastFullRunTime() {
		return totalUsersCache.get(CACHE_TYPE.LAST_FULLRUN_TIME);
	}

	@SneakyThrows(value = ExecutionException.class)
	@Override
	public int getShowCodeUsers() {
		return totalUsersCache.get(CACHE_TYPE.SHOW_CODE_USERS);
	}

	@Override
	public int getCheckRuns() {
		return counterCheckRuns.size();
	}

	@Override
	public void incCheckRuns() {
		counterCheckRuns.add(new Date());
	}

	@Override
	public int getActiveFullRuns() {
		return GameServer.getTcpHandler().getTpeFullRun().getActiveCount();
	}

	@Override
	public int getQueuedFullRuns() {
		return GameServer.getTcpHandler().getTpeFullRun().getQueue().size();
	}

	@Override
	public int getActiveTestRuns() {
		return GameServer.getTcpHandler().getTpeTestRun().getActiveCount();
	}
	
	@Override
	public int getQueuedTestRuns() {
		return GameServer.getTcpHandler().getTpeTestRun().getQueue().size();
	}
}
