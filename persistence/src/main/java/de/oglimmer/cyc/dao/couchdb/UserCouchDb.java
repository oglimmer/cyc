package de.oglimmer.cyc.dao.couchdb;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;

import de.oglimmer.cyc.dao.UserDao;
import de.oglimmer.cyc.model.User;

public class UserCouchDb extends CouchDbRepositorySupport<User> implements UserDao {

	public UserCouchDb(CouchDbConnector db) {
		super(User.class, db);
		initStandardDesignDocument();
	}

	@GenerateView
	public List<User> findByUsername(String username) {
		return queryView("by_username", username);
	}

	@Override
	public List<User> findAllUser() {
		return queryView("by_username");
	}

	@Override
	public List<User> findByEmail(String email) {
		return queryView("by_email", email);
	}

	@Override
	public int findByOpenSource(String username) {
		ViewQuery vq = createQuery("by_openSource").includeDocs(false).key(username);
		return db.queryView(vq).getSize();
	}
}
