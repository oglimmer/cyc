package de.oglimmer.cyc.dao.couchdb;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;

import de.oglimmer.cyc.dao.GameWinnersDao;
import de.oglimmer.cyc.model.GameWinners;

public class GameWinnersCouchDb extends CouchDbRepositorySupport<GameWinners> implements GameWinnersDao {

	public GameWinnersCouchDb(CouchDbConnector db) {
		super(GameWinners.class, db);
		initStandardDesignDocument();
	}

	@Override
	public List<GameWinners> findAllGameWinners(int minutes, Date maxDate) {
		ViewQuery q = createQuery("all");
		q.includeDocs(true);
		q.descending(true);
		setStartEndKey(q, minutes, maxDate);
		return db.queryView(q, type);
	}

	private void setStartEndKey(ViewQuery q, int minutes, Date maxDate) {		
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime maxZDT = ZonedDateTime.ofInstant(maxDate.toInstant(), ZoneOffset.UTC);
		if (now.isAfter(maxZDT)) {
			ZonedDateTime startKeyDate = maxZDT.minusSeconds(1);
			ZonedDateTime endKeyDate = maxZDT.minusMinutes(minutes);
			q.startKey(Date.from(startKeyDate.toInstant()));
			q.endKey(Date.from(endKeyDate.toInstant()));
		} else {
			ZonedDateTime endKeyDate = now.minusMinutes(minutes);
			q.endKey(Date.from(endKeyDate.toInstant()));
		}
	}

}
