package de.oglimmer.cyc.web.actions;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.cyc.api.GameResult;
import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.DoesNotRequireLogin;

@DoesNotRequireLogin
public class GameRunDetailsActionBean extends BaseAction {

	private GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());
	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	private GameResult result;
	private Date startTime;
	private Date endTime;
	private long memUsed;
	private List<String> participants;
	private String username;
	private Map<String, Boolean> showCode = new HashMap<>();

	public GameResult getResult() {
		return result;
	}

	public void setResult(GameResult result) {
		this.result = result;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getMemUsed() {
		return memUsed;
	}

	public void setMemUsed(long memUsed) {
		this.memUsed = memUsed;
	}

	public Collection<String> getParticipants() {
		return participants;
	}

	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map<String, Boolean> getShowCode() {
		return showCode;
	}

	public void setShowCode(Map<String, Boolean> showCode) {
		this.showCode = showCode;
	}

	@Before
	public void getNextRunFromGameEngine() {
		String userId = (String) getContext().getRequest().getSession().getAttribute("userid");
		if (userId != null) {
			User user = userDao.get(userId);
			username = user.getUsername();
		}

		String gameRunId = getContext().getRequest().getParameter("gameRunId");
		GameRun gr = null;
		if (gameRunId == null || gameRunId.isEmpty()) {
			List<GameRun> listGameRuns = dao.findAllGameRun(1);
			if (listGameRuns.size() > 0) {
				gr = listGameRuns.get(0);
			}
		} else {
			gr = dao.get(gameRunId);
		}
		if (gr != null) {
			gr.getResult().sortPlayers();
			setResult(gr.getResult());
			setMemUsed(gr.getMemUsed());
			setStartTime(gr.getStartTime());
			setEndTime(gr.getEndTime());
			setParticipants(gr.getParticipants());
			for (String p : gr.getParticipants()) {
				List<User> openSourceUsers = userDao.findByOpenSource(p.toLowerCase());
				if (openSourceUsers.size() > 0) {
					showCode.put(p, true);
				}
			}
		}
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution("/WEB-INF/jsp/gameRunDetails.jsp");
	}

}