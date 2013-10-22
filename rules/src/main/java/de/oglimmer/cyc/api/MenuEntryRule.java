package de.oglimmer.cyc.api;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;

public class MenuEntryRule {

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
						EstablishmentRule.class.getResourceAsStream("/de.oglimmer.cyc.api.MenuEntryRules.groovy"),
						"de.oglimmer.cyc.api.MenuEntryRules.groovy");

				// let's call some method on an instance
				groovyObject = (GroovyObject) groovyClass.newInstance();
			}
		} catch (CompilationFailedException | InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * base deliciouness is 5. max 10, min 0.
	 */
	int getDeliciousness(Object ingredients, Object price, Class<?> foodClass) {
		groovyObject.setProperty("ingredients", ingredients);
		groovyObject.setProperty("price", price);
		groovyObject.setProperty("Food", foodClass);
		Object[] argss = {};
		return (Integer) groovyObject.invokeMethod("run", argss);
	}
}