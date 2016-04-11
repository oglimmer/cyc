package de.oglimmer.cyc.web.action;

import java.util.Date;
import java.util.List;

import de.oglimmer.cyc.dao.GameWinnersDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameWinnersCouchDb;
import de.oglimmer.cyc.model.GameWinners;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.ThreeDaysWinner;
import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

@DoesNotRequireLogin
public class RunHistoryActionBean extends BaseAction {
	private static final String VIEW = "/WEB-INF/jsp/runHistory.jsp";

	private GameWinnersDao dao = new GameWinnersCouchDb(CouchDbUtil.getDatabase());

	@Getter
	@Setter
	private List<GameWinners> runHistory;
	
	@Getter
	private String timeRange;

	@Before
	public void loadRunHistory() {
		// get last three days
		Date[] startEndDate = ThreeDaysWinner.INSTANCE.getStartEndKey();
		runHistory = dao.findAllGameWinners(startEndDate[0], startEndDate[1]);
		timeRange = ThreeDaysWinner.INSTANCE.dateToString(startEndDate);
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	public Resolution back() {
		return new RedirectResolution(PortalActionBean.class);
	}
}
