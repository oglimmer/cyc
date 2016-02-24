package de.oglimmer.cyc.web.action;

import java.text.DateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.GameExecutor;
import de.oglimmer.cyc.web.GameScheduler;
import de.oglimmer.cyc.web.ThreeDaysWinner;
import de.oglimmer.cyc.web.WebContainerProperties;
import de.oglimmer.cyc.web.exception.CycPermissionException;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.LifecycleStage;

public class PortalActionBean extends BaseAction {
	private static Logger log = LoggerFactory.getLogger(PortalActionBean.class);

	private static final String VIEW = "/WEB-INF/jsp/portal.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());

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
	private String lastWinner;

	@Getter
	@Setter
	private String[] threeDayWinner;

	@Getter
	@Setter
	private boolean fullRun;
	@Getter
	@Setter
	private boolean testRun;
	@Getter
	@Setter
	private boolean openSource;
	@Getter
	@Setter
	private String companyName;

	@Getter
	private int editorHeight;

	@Before(stages = { LifecycleStage.EventHandling })
	public void getNextRunFromGameEngine() {
		Date nextRunDate = GameScheduler.INSTANCE.getNextRun();
		DateFormat dateTimeDf = DateFormat.getDateTimeInstance();
		setNextRun(dateTimeDf.format(nextRunDate));

		ThreeDaysWinner.Result result = ThreeDaysWinner.INSTANCE.calcThreeDayWinner();
		threeDayWinner = result.getThreeDayWinner();
		lastWinner = result.getLastWinner();
	}

	private int countLines(String javaCode) {
		if (javaCode == null) {
			return 0;
		}
		return javaCode.split("\r\n|\r|\n").length;
	}

	@Before(on = { "show" })
	public void loadFromDb() {
		User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		company = user.getMainJavaScript();
		companyName = user.getUsername();
		output = user.getLastError();
		lastRun = user.getLastPrivateRun();
		fullRun = user.getPermission() > 0;
		openSource = user.getOpenSource() > 0;
		
		testRun = WebContainerProperties.INSTANCE.getSystemDisabledDate().after(new Date());

		String userAgent = getContext().getRequest().getHeader("User-Agent").toLowerCase();
		boolean isMobile = userAgent.matches("(?i).*(ipad|iphone|android).*");
		editorHeight = isMobile ? (countLines(company) + 30) * 16 : 500;
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	public Resolution exit() {
		getContext().getRequest().getSession().invalidate();
		getContext().getResponse().addCookie(new Cookie("noFbLogin", "true"));
		return new RedirectResolution(LandingActionBean.class);
	}

	public Resolution saveRun() {
		if (WebContainerProperties.INSTANCE.getSystemDisabledDate().before(new Date())) {
			output = "System disabled.";
			return new ForwardResolution("/WEB-INF/jsp/ajax/portalSave.jsp");
		}
		User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		user.setMainJavaScript(getCompany());
		user.setActive(true);
		user.setLastCodeChangeDate(new Date());
		userDao.update(user);

		try {
			output = GameExecutor.INSTANCE.startTestRun((String) getContext().getRequest().getSession().getAttribute("userid"));
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
				GameExecutor.INSTANCE.startFullRun();
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
			json.put("nextRun", getNextRun());
			json.put("lastWinner", getLastWinner());
			json.put("threeDayWinner0", getThreeDayWinner()[0]);
			json.put("threeDayWinner1", getThreeDayWinner()[1]);
			json.put("threeDayWinner2", getThreeDayWinner()[2]);
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