package de.oglimmer.cyc.web.action;

import java.util.List;

import de.oglimmer.cyc.model.GameWinners;
import de.oglimmer.cyc.web.DoesNotRequireLogin;
import de.oglimmer.cyc.web.winner.WinnerResult;
import de.oglimmer.cyc.web.winner.WinnerHistoryCalculation;
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

	@Getter
	@Setter
	private List<GameWinners> runHistory;
	
	@Getter
	private String timeRange;

	@Before
	public void loadRunHistory() {
		// get last three days
		WinnerResult result = WinnerHistoryCalculation.INSTANCE.calc();
		runHistory = result.getGameWinnersList();
		timeRange = result.getThreeDayWinnerTimeRange().toString();
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution(VIEW);
	}

	public Resolution back() {
		return new RedirectResolution(PortalActionBean.class);
	}
}
