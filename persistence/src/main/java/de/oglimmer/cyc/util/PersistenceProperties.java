package de.oglimmer.cyc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.SneakyThrows;

public enum PersistenceProperties {
	INSTANCE;

	private Properties prop = new Properties();

	@SneakyThrows(value = IOException.class)
	private PersistenceProperties() {
		if (System.getProperty("cyc.properties") != null) {
			try (FileInputStream fis = new FileInputStream(System.getProperty("cyc.properties"))) {
				prop.load(fis);
				System.out.println("Successfully loaded cyc.properties from {}", System.getProperty("cyc.properties"));
			}
		}
	}

	public String getCouchDbHost() {
		return prop.getProperty("couchdb.host", "localhost");
	}

	public int getCouchDbPort() {
		return Integer.parseInt(prop.getProperty("couchdb.port", "5984"));
	}

	public String getCouchDbUser() {
		return prop.getProperty("couchdb.user", "");
	}

	public String getCouchDbPassword() {
		return prop.getProperty("couchdb.password", "");
	}

}
