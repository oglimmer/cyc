package de.oglimmer.cyc.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import de.oglimmer.cyc.api.GameResult;
import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

@DoesNotRequireLogin
public class GameRunDetailsActionBean extends BaseAction {

	private GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());
	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

	@Getter
	@Setter
	private GameResult result;
	@Getter
	@Setter
	private Date startTime;
	@Getter
	@Setter
	private Date endTime;
	@Getter
	@Setter
	private List<String> participants;
	@Getter
	@Setter
	private String username;
	@Getter
	@Setter
	private Map<String, Boolean> showCode = new HashMap<>();

	@Before
	public void getNextRunFromGameEngine() {
		HttpSession httpSession = getContext().getRequest().getSession(false);
		if (httpSession != null) {
			String userId = (String) httpSession.getAttribute("userid");
			if (userId != null) {
				User user = userDao.get(userId);
				username = user.getUsername();
			}
		}

		String gameRunId = getContext().getRequest().getParameter("gameRunId");
		GameRun gr = null;
		if (gameRunId == null || gameRunId.isEmpty()) {
			List<GameRun> listGameRuns = dao.findAllGameRun(1);
			if (!listGameRuns.isEmpty()) {
				gr = listGameRuns.get(0);
			}
		} else {
			try {
				gr = dao.get(gameRunId);
			} catch (org.ektorp.DocumentNotFoundException e) {
				// happens a lot via bots/crawlers
			}
		}
		if (gr != null) {
			gr.getResult().sortPlayers();
			setResult(gr.getResult());
			setStartTime(gr.getStartTime());
			setEndTime(gr.getEndTime());
			setParticipants(gr.getParticipants());			
			for (String p : userDao.findByOpenSource(gr.getParticipants())) {
				showCode.put(p, true);
			}			
		}
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution("/WEB-INF/jsp/gameRunDetails.jsp");
	}

}