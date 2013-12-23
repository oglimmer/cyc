package de.oglimmer.cyc.web.actions;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.GameExecutor;
import de.oglimmer.cyc.web.exception.CycPermissionException;

public class PortalActionBean extends BaseAction {
	private static Logger log = LoggerFactory.getLogger(PortalActionBean.class);

	private static final String VIEW = "/WEB-INF/jsp/portal.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());
	private GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());

	@Getter
	@Setter
	private String company;
	@Getter
	@Setter
	private String output;
	@Getter
	@Setter
	private Date lastRun;

	@Getter
	@Setter
	private String nextRun;
	@Getter
	@Setter
	private int totalRuns;
	@Getter
	@Setter
	private String lastWinner;

	@Getter
	@Setter
	private boolean fullRun;
	@Getter
	@Setter
	private boolean openSource;

	@Before
	public void getNextRunFromGameEngine() {
		Date date = GameExecutor.INSTANCE.getNextRun();
		DateFormat df = DateFormat.getDateTimeInstance();
		setNextRun(df.format(date));

		List<GameRun> listGameRuns = dao.findAllGameRun(1);
		setTotalRuns(dao.sizeAllGameRun());
		if (listGameRuns.isEmpty()) {
			setLastWinner("-");
		} else {
			GameRun lastGameRun = listGameRuns.get(0);
			setLastWinner(lastGameRun.getResult().getWinner());
		}
	}

	@Before(on = { "show" })
	public void loadFromDb() {
		User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		company = user.getMainJavaScript();
		output = user.getLastError();
		lastRun = user.getLastPrivateRun();
		fullRun = user.getPermission() > 0;
		openSource = user.getOpenSource() > 0;
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	public Resolution exit() {
		getContext().getRequest().getSession().removeAttribute("userid");
		return new RedirectResolution(LandingActionBean.class);
	}

	public Resolution saveRun() {
		User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		user.setMainJavaScript(getCompany());
		user.setActive(true);
		user.setLastCodeChangeDate(new Date());
		userDao.update(user);

		try {
			GameExecutor.INSTANCE.runGame((String) getContext().getRequest().getSession().getAttribute("userid"));
			output = "Saved & check run queued.";
		} catch (Exception e) {
			log.debug("Failed to run check run", e);
			output = "Internal server error";
		}

		return new ForwardResolution("/WEB-INF/jsp/ajax/portalSave.jsp");
	}

	public Resolution fullRun() {
		String userId = (String) getContext().getRequest().getSession().getAttribute("userid");
		try {
			User user = userDao.get(userId);
			if (user.getPermission() > 0) {
				GameExecutor.INSTANCE.runGame(null);
				output = "Global run started ... wait";
			} else {
				throw new CycPermissionException("User " + userId + " has no permission to start a full run");
			}
		} catch (Exception e) {
			log.debug("Failed to run check run", e);
			output = "Internal server error";
		}
		return new ForwardResolution("/WEB-INF/jsp/ajax/portalSave.jsp");
	}

	public Resolution checkForUpdateGlobalRun() {
		try {
			getNextRunFromGameEngine();
			JSONObject json = new JSONObject();
			json.put("lastWinner", getLastWinner());
			json.put("totalRuns", getTotalRuns());
			json.put("nextRun", getNextRun());
			output = json.toString();
		} catch (JSONException e) {
			log.error("Failed to create JSON response", e);
		}

		return new ForwardResolution("/WEB-INF/jsp/ajax/portalSave.jsp");
	}

	public Resolution checkForUpdateSaveTest() {
		try {
			User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
			JSONObject json = new JSONObject();
			json.put("lastRun", user.getLastPrivateRun());
			json.put("html", user.getLastError());

			output = json.toString();
		} catch (JSONException e) {
			log.error("Failed to create JSON response", e);
		}

		return new ForwardResolution("/WEB-INF/jsp/ajax/portalSave.jsp");
	}

	public Resolution openSourceChangedOn() {
		User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		user.setOpenSource(1);
		userDao.update(user);
		return new ForwardResolution("/WEB-INF/jsp/ajax/portalSave.jsp");
	}

	public Resolution openSourceChangedOff() {
		User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		user.setOpenSource(0);
		userDao.update(user);
		return new ForwardResolution("/WEB-INF/jsp/ajax/portalSave.jsp");
	}

}