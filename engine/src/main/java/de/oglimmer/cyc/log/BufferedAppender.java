package de.oglimmer.cyc.log;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.core.OutputStreamAppender;

public class BufferedAppender<E> extends OutputStreamAppender<E> {

	private static Map<String, BufferedAppender<?>> registry = new HashMap<>();

	public static BufferedAppender<?> getInstance(String name) {
		return registry.get(name);
	}

	@Override
	public void start() {
		OutputStream targetStream = new ByteArrayOutputStream();
		setOutputStream(targetStream);
		super.start();
		registry.put(getName(), this);
	}

	public String getFullOutput() {
		return ((ByteArrayOutputStream) getOutputStream()).toString();
	}

}
