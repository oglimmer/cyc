package de.oglimmer.cyc.dao.couchdb;

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
	public List<GameWinners> findAllGameWinners(Date startDate, Date endDate) {
		ViewQuery q = createQuery("all");
		q.includeDocs(false);
		q.descending(true);
		if (startDate != null) {
			q.startKey(startDate);
		}
		if (endDate != null) {
			q.endKey(endDate);
		}
		return db.queryView(q, type);
	}

}
