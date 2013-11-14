package de.oglimmer.cyc.api;

import java.util.Collection;

public enum GuestRule {
	INSTACE;

	private GroovyInitializer<IGuestRule> ini;

	private GuestRule() {
		ini = new GroovyInitializer<>();
		ini.loadGroovyScript("de.oglimmer.cyc.api.GuestRules.groovy");
	}

	public Collection<?> selectMenu(Object c) {
		return ini.getGroovyObject().selectMenu(c);
	}

}
