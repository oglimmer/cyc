package de.oglimmer.cyc.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public enum GuestRule {
	INSTACE;

	private GroovyInitializer<IGuestRule> ini;
	private Object impl;
	private Method selectMenu;

	private GuestRule() {
		try {
			Class<?> clazz = Class.forName("de.oglimmer.cyc.api.GuestRuleImpl");
			impl = clazz.newInstance();
			selectMenu = clazz.getMethod("selectMenu", new Class[] { Object.class });
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
				| SecurityException e) {
			System.out.println("Using grooving for GuestRule");
			ini = new GroovyInitializer<>();
			ini.loadGroovyScript("de.oglimmer.cyc.api.GuestRules.groovy");
		}

	}

	public Collection<?> selectMenu(Object c) {
		if (impl == null) {
			return ini.getGroovyObject().selectMenu(c);
		} else {
			try {
				return (Collection<?>) selectMenu.invoke(impl, c);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
