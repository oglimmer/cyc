package de.oglimmer.cyc.dao.couchdb;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult.Row;
import org.ektorp.support.CouchDbRepositorySupport;

import de.oglimmer.cyc.dao.GameRunDao;
import de.oglimmer.cyc.model.GameRun;

public class GameRunCouchDb extends CouchDbRepositorySupport<GameRun> implements GameRunDao {

	public GameRunCouchDb(CouchDbConnector db) {
		super(GameRun.class, db);
		initStandardDesignDocument();
	}

	@Override
	public List<GameRun> findAllGameRun(int limit) {
		ViewQuery q = createQuery("by_result");
		q.includeDocs(true);
		q.descending(true);
		if (limit != -1) {
			q.limit(limit);
		}
		return db.queryView(q, type);
	}

	@Override
	public int sizeAllGameRun() {
		ViewQuery q = createQuery("count");
		List<Row> rows = db.queryView(q).getRows();
		return rows.isEmpty() ? 0 : rows.get(0).getValueAsInt();
	}
}
