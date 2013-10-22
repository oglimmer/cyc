package de.oglimmer.cyc.api;

import groovy.lang.GroovyClassLoader;

import java.io.IOException;
import java.util.Collection;

import org.codehaus.groovy.control.CompilationFailedException;

public class GuestRule {

	static ClassLoader parentCL;
	static GroovyClassLoader loader;
	static Class<?> groovyClass;
	static IGuestRule groovyObject;

	static void init() {
		try {
			parentCL = EstablishmentRule.class.getClassLoader();
			try (GroovyClassLoader loader = new GroovyClassLoader(parentCL)) {
				@SuppressWarnings("deprecation")
				Class<?> groovyClass = loader.parseClass(
						EstablishmentRule.class.getResourceAsStream("/de.oglimmer.cyc.api.GuestRules.groovy"),
						"de.oglimmer.cyc.api.GuestRules.groovy");

				// let's call some method on an instance
				groovyObject = (IGuestRule) groovyClass.newInstance();
			}
		} catch (CompilationFailedException | InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
	}

	public Collection<?> selectMenu(Object c) {
		return groovyObject.selectMenu(c);
	}

}
