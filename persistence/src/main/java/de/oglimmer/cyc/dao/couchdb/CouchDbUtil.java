package de.oglimmer.cyc.dao.couchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import de.oglimmer.cyc.util.PersistenceProperties;

public class CouchDbUtil {

	private CouchDbUtil() {
		// no code here
	}

	private static HttpClient httpClient;
	private static CouchDbInstance dbInstance;
	private static CouchDbConnector db;

	static {
		StdHttpClient.Builder builder = new StdHttpClient.Builder();
		builder.username(PersistenceProperties.INSTANCE.getCouchDbUser());
		builder.password(PersistenceProperties.INSTANCE.getCouchDbPassword());
		builder.host(PersistenceProperties.INSTANCE.getCouchDbHost());
		builder.port(PersistenceProperties.INSTANCE.getCouchDbPort());
		if (System.getProperty("http.proxyHost") != null) {
			builder.proxy(System.getProperty("http.proxyHost"));
		}
		if (System.getProperty("Dhttp.proxyPort") != null) {
			builder.proxyPort(Integer.parseInt(System.getProperty("Dhttp.proxyPort")));
		}
		httpClient = builder.build();
		dbInstance = new StdCouchDbInstance(httpClient);
		db = dbInstance.createConnector("cyc", false);
	}

	public static CouchDbConnector getDatabase() {
		return db;
	}

	public static void shutdown() {
		httpClient.shutdown();
	}
}
