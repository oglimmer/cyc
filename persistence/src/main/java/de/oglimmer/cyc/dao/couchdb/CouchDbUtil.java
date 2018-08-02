package de.oglimmer.cyc.dao.couchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.IdleConnectionMonitor;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import de.oglimmer.cyc.util.PersistenceProperties;

public class CouchDbUtil {

	public static CouchDbUtil INSTANCE = new CouchDbUtil();

	private HttpClient httpClient;
	private CouchDbInstance dbInstance;
	private CouchDbConnector db;

	private CouchDbUtil() {
		StdHttpClient.Builder builder = new StdHttpClient.Builder();
		String user = PersistenceProperties.INSTANCE.getCouchDbUser();
		if(user != null && !user.trim().isEmpty()) {
			builder.username(user);
		}
		String password = PersistenceProperties.INSTANCE.getCouchDbPassword();
		if(password != null && !password.trim().isEmpty()) {
			builder.password(password);
		}
		builder.host(PersistenceProperties.INSTANCE.getCouchDbHost());
		builder.port(PersistenceProperties.INSTANCE.getCouchDbPort());
		if (System.getProperty("http.proxyHost") != null) {
			builder.proxy(System.getProperty("http.proxyHost"));
		}
		if (System.getProperty("http.proxyPort") != null) {
			builder.proxyPort(Integer.parseInt(System.getProperty("http.proxyPort")));
		}
		httpClient = builder.build();
		dbInstance = new StdCouchDbInstance(httpClient);
		db = dbInstance.createConnector("cyc", false);
	}

	public CouchDbConnector getDatabase() {
		return db;
	}

	public void shutdown() {
		httpClient.shutdown();
		IdleConnectionMonitor.shutdown();
	}
}
