package de.oglimmer.cyc.engineContainer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.SneakyThrows;

public enum EngineContainerProperties {
	INSTANCE;

	private Properties prop = new Properties();

	@SneakyThrows(value = IOException.class)
	private EngineContainerProperties() {
		if (System.getProperty("cyc.properties") != null) {
			try (FileInputStream fis = new FileInputStream(
					System.getProperty("cyc.properties"))) {
				prop.load(fis);
			}
		}
	}

	public String getBindAddress() {
		// don't use localhost, as JDK8 doesn't resolve localhost properly
		return prop.getProperty("bind", "127.0.0.1");
	}

}
