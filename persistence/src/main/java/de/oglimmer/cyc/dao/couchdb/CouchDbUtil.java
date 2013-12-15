package de.oglimmer.cyc.dao.couchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class CouchDbUtil {

	private static HttpClient httpClient;
	private static CouchDbInstance dbInstance;
	private static CouchDbConnector db;

	static {
		StdHttpClient.Builder builder = new StdHttpClient.Builder();
		builder.host("localhost");
		builder.port(5984);
		// builder.proxy("localhost");
		// builder.proxyPort(8888);
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
