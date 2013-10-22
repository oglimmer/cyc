package de.oglimmer.cyc.web.actions;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.dao.couchdb.UserCouchDb;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.model.User;
import de.oglimmer.cyc.web.GameExecutor;

public class PortalActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/portal.jsp";

	private UserDao userDao = new UserCouchDb(CouchDbUtil.getDatabase());
	private GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());

	private String company;
	private String output;

	private String nextRun;
	private int totalRuns;
	private String lastWinner;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getNextRun() {
		return nextRun;
	}

	public void setNextRun(String nextRun) {
		this.nextRun = nextRun;
	}

	public int getTotalRuns() {
		return totalRuns;
	}

	public void setTotalRuns(int totalRuns) {
		this.totalRuns = totalRuns;
	}

	public String getLastWinner() {
		return lastWinner;
	}

	public void setLastWinner(String lastWinner) {
		this.lastWinner = lastWinner;
	}

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
			GameRun lastRun = listGameRuns.get(0);
			setLastWinner(lastRun.getResult().getWinner());
		}
	}

	@Before(on = { "show" })
	public void loadFromDb() {
		User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		company = user.getMainJavaScript();
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	public Resolution exit() {
		getContext().getRequest().getSession().removeAttribute("userid");
		return new RedirectResolution(LandingActionBean.class);
	}

	public Resolution save() {
		User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		user.setMainJavaScript(getCompany());
		user.setActive(true);
		userDao.update(user);

		return new ForwardResolution("/WEB-INF/jsp/portal.jsp");
	}

	public Resolution saveRun() {
		User user = userDao.get((String) getContext().getRequest().getSession().getAttribute("userid"));
		user.setMainJavaScript(getCompany());
		user.setActive(true);
		userDao.update(user);

		output = GameExecutor.INSTANCE.runGame((String) getContext().getRequest().getSession().getAttribute("userid"));

		return new ForwardResolution("/WEB-INF/jsp/portal.jsp");
	}

}