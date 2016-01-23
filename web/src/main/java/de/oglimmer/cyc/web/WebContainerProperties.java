package de.oglimmer.cyc.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.SneakyThrows;

public enum WebContainerProperties {
	INSTANCE;

	private Properties prop = new Properties();

	@SneakyThrows(value = IOException.class)
	private WebContainerProperties() {
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

	public String getGoogleClientId() {
		return prop.getProperty("google.clientId", "");
	}

	public String getEngineHost() {
		// don't use localhost, as JDK8 doesn't resolve localhost properly
		return prop.getProperty("engine.host", "127.0.0.1");
	}

	public int getEnginePort() {
		/* port defined in de.oglimmer.cyc.GameServer.SERVER_PORT */
		return Integer.parseInt(prop.getProperty("engine.port", "9998"));
	}

	public boolean isHttpsEnabled() {
		return Boolean.parseBoolean(prop.getProperty("web.https.required", "false"));
	}

	public String getHttpsDomain() {
		return prop.getProperty("web.https.domain", "codeyourrestaurant.com");
	}

	public String getSmtpUser() {
		return prop.getProperty("smtp.user", System.getProperty("cyc.smtp.user", ""));
	}

	public String getSmtpPassword() {
		return prop.getProperty("smtp.password", System.getProperty("cyc.smtp.password", ""));
	}

	public String getSmtpHost() {
		return prop.getProperty("smtp.host", System.getProperty("cyc.smtp.host", "localhost"));
	}

	public int getSmtpPort() {
		return Integer.parseInt(prop.getProperty("smtp.port", System.getProperty("cyc.smtp.port", "-1")));
	}

	public boolean getSmtpSSL() {
		return Boolean.parseBoolean(prop.getProperty("smtp.ssl", System.getProperty("cyc.smtp.ssl", "false")));
	}

	public String getSmtpFrom() {
		return prop.getProperty("smtp.from", System.getProperty("cyc.smtp.from",
				"\"CodeYourRestaurant Mail System\" <no-reply@codeyourrestaurant.com>"));
	}
}
