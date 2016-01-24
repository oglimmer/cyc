package de.oglimmer.cyc.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum EstablishmentRule {
	INSTACE;

	private GroovyInitializer<IEstablishmentRule> ini;
	private Object impl;
	private Method getScore;

	private EstablishmentRule() {
		try {
			Class<?> clazz = Class.forName("de.oglimmer.cyc.api.EstablishmentRuleImpl");
			impl = clazz.newInstance();
			getScore = clazz.getMethod("getScore", new Class[] { Object.class });
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
				| SecurityException e) {
			System.out.println("Using grooving for EstablishmentRule");
			ini = new GroovyInitializer<>();
			ini.loadGroovyScript("de.oglimmer.cyc.api.EstablishmentRules.groovy");
		}
	}

	int getScore(Object est, Object log) {
		if (impl == null) {
			return ini.getGroovyObject().getScore(est);
		} else {
			try {
				return (int) getScore.invoke(impl, est);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
