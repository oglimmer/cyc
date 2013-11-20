package de.oglimmer.cyc.dao.couchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class CouchDbUtil {

	private static HttpClient httpClient = new StdHttpClient.Builder().build();
	private static CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
	private static CouchDbConnector db = dbInstance.createConnector("cyc", false);

	public static CouchDbConnector getDatabase() {
		return db;
	}

	public static void shutdown() {
		httpClient.shutdown();
	}
}
