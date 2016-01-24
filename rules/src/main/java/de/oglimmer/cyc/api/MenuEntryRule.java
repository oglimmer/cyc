package de.oglimmer.cyc.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum MenuEntryRule {
	INSTACE;

	private GroovyInitializer<IMenuEntryRule> ini;
	private Object impl;
	private Method getDeliciousness;

	private MenuEntryRule() {
		try {
			Class<?> clazz = Class.forName("de.oglimmer.cyc.api.MenuEntryRuleImpl");
			impl = clazz.newInstance();
			getDeliciousness = clazz.getMethod("getDeliciousness", new Class[] { Object.class });
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
				| SecurityException e) {
			System.out.println("Using grooving for MenuEntryRule");
			ini = new GroovyInitializer<>();
			ini.loadGroovyScript("de.oglimmer.cyc.api.MenuEntryRules.groovy");
		}
	}

	/**
	 * base deliciousness is 5. max 10, min 0.
	 */
	int getDeliciousness(Object ingredients) {
		if (impl == null) {
			return ini.getGroovyObject().getDeliciousness(ingredients);
		} else {
			try {
				return (int) getDeliciousness.invoke(impl, ingredients);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
