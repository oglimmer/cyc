package de.oglimmer.cyc.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.SneakyThrows;

public enum CyrProperties {
	INSTANCE;

	private Properties prop = new Properties();

	@SneakyThrows(value = IOException.class)
	private CyrProperties() {
		if (System.getProperty("cyc.properties") != null) {
			try (FileInputStream fis = new FileInputStream(System.getProperty("cyc.properties"))) {
				prop.load(fis);
			}
		}
	}

	public String getFbSecret() {
		return prop.getProperty("facebook.secret", "");
	}

	public String getFbAppId() {
		return prop.getProperty("facebook.appId", "");
	}

}
