package de.oglimmer.cyc.dao.couchdb;

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
	public List<GameWinners> findAllGameWinners(int limit) {
		ViewQuery q = createQuery("all");
		q.includeDocs(true);
		q.descending(true);
		if (limit != -1) {
			q.limit(limit);
		}
		return db.queryView(q, type);
	}

}
