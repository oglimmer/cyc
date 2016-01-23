package de.oglimmer.cyc.dao.couchdb;

import java.util.Collections;
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
	@Override
	public List<User> findByUsername(String username) {
		if (username == null) {
			return Collections.emptyList();
		}
		return queryView("by_username", username.toLowerCase());
	}

	@Override
	public List<User> findAllUser() {
		return queryView("by_username");
	}

	@Override
	public List<User> findByEmail(String email) {
		if (email == null) {
			return Collections.emptyList();
		}
		return queryView("by_email", email.toLowerCase());
	}

	@Override
	public int findByOpenSource(String username) {
		ViewQuery vq = createQuery("by_openSource").includeDocs(false).key(username);
		return db.queryView(vq).getSize();
	}

	@Override
	public List<User> findByFBUserId(String userId) {
		return queryView("by_fbUserId", userId);
	}

	@Override
	public List<User> findByGoogleUserId(String userId) {
		return queryView("by_googleUserId", userId);
	}
}
