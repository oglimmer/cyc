package de.oglimmer.cyc.web.actions;

import java.util.List;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.model.GameRun;
import de.oglimmer.cyc.web.DoesNotRequireLogin;

@DoesNotRequireLogin
public class RunHistoryActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/runHistory.jsp";

	private GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());

	private List<GameRun> runHistory;

	public void setRunHistory(List<GameRun> runHistory) {
		this.runHistory = runHistory;
	}

	public List<GameRun> getRunHistory() {
		return runHistory;
	}

	@Before
	public void loadRunHistory() {
		runHistory = dao.findAllGameRun(288);// last three days
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	public Resolution back() {
		return new RedirectResolution(PortalActionBean.class);
	}
}
