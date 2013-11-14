package de.oglimmer.cyc.api;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroovyInitializer<T> {

	private static Logger log = LoggerFactory.getLogger(GroovyInitializer.class);

	public static void globalInit() {
		EstablishmentRule.INSTACE.toString();
		MenuEntryRule.INSTACE.toString();
		GuestRule.INSTACE.toString();
	}

	private GroovyObject groovyObject;

	public void loadGroovyScript(String filename) {
		try {
			ClassLoader parentCL = GroovyInitializer.class.getClassLoader();
			try (GroovyClassLoader loader = new GroovyClassLoader(parentCL)) {
				@SuppressWarnings("deprecation")
				Class<?> groovyClass = loader.parseClass(GroovyInitializer.class.getResourceAsStream("/" + filename),
						filename);

				groovyObject = (GroovyObject) groovyClass.newInstance();
				groovyObject.setProperty("log", LoggerFactory.getLogger(filename));
			}
		} catch (CompilationFailedException | InstantiationException | IllegalAccessException | IOException e) {
			log.error("Failed to init groovy for " + filename, e);
		}
	}

	@SuppressWarnings("unchecked")
	public T getGroovyObject() {
		return (T) groovyObject;
	}

}
