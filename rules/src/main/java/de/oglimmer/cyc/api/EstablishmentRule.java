package de.oglimmer.cyc.api;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;

public class EstablishmentRule {

	static ClassLoader parentCL;
	static GroovyClassLoader loader;
	static Class<?> groovyClass;
	static GroovyObject groovyObject;

	static void init() {
		try {
			parentCL = EstablishmentRule.class.getClassLoader();
			try (GroovyClassLoader loader = new GroovyClassLoader(parentCL)) {
				@SuppressWarnings("deprecation")
				Class<?> groovyClass = loader.parseClass(
						EstablishmentRule.class.getResourceAsStream("/de.oglimmer.cyc.api.EstablishmentRules.groovy"),
						"de.oglimmer.cyc.api.EstablishmentRules.groovy");

				// let's call some method on an instance
				groovyObject = (GroovyObject) groovyClass.newInstance();
			}
		} catch (CompilationFailedException | InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
	}

	int getScore(Object est, Object log, Class<?> jobPositionClass, Class<?> interiorAccessoryClass) {
		groovyObject.setProperty("thiz", est);
		groovyObject.setProperty("log", log);
		groovyObject.setProperty("JobPosition", jobPositionClass);
		groovyObject.setProperty("InteriorAccessory", interiorAccessoryClass);
		Object[] argss = {};
		return (Integer) groovyObject.invokeMethod("run", argss);
	}

}
