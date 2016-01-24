package de.oglimmer.cyc.web.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import lombok.SneakyThrows;

public enum DefaultCode {
	INSTANCE;

	@SneakyThrows(value = IOException.class)
	public String getDefaultCode() {
		return IOUtils.toString(this.getClass().getResourceAsStream("/default_code.js"), "UTF-8");
	}

}
