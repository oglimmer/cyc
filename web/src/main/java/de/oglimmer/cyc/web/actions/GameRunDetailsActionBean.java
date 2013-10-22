package de.oglimmer.cyc.web.actions;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.cyc.api.GameResult;
import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.dao.couchdb.CouchDbUtil;
import de.oglimmer.cyc.dao.couchdb.GameRunCouchDb;
import de.oglimmer.cyc.model.GameRun;

public class GameRunDetailsActionBean extends BaseAction {

	private GameRunDao dao = new GameRunCouchDb(CouchDbUtil.getDatabase());

	private GameResult result;
	private Date startTime;
	private Date endTime;
	private long memUsed;
	private List<String> participants;

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

	@Before
	public void getNextRunFromGameEngine() {
		String gameRunId = getContext().getRequest().getParameter("gameRunId");
		GameRun gr = null;
		if (gameRunId == null) {
			List<GameRun> listGameRuns = dao.findAllGameRun(1);
			if (listGameRuns.size() > 0) {
				gr = listGameRuns.get(0);
			}
		} else {
			gr = dao.get(gameRunId);
		}
		if (gr != null) {
			setResult(gr.getResult());
			setMemUsed(gr.getMemUsed());
			setStartTime(gr.getStartTime());
			setEndTime(gr.getEndTime());
			setParticipants(gr.getParticipants());
		}
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution("/WEB-INF/jsp/gameRunDetails.jsp");
	}

}