package de.oglimmer.cyc.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {

	final private ThreadFactory delegateFactory = Executors.defaultThreadFactory();
	private String name;
	private int counter;

	public NamedThreadFactory(String name) {
		this.name = name;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread newThread = delegateFactory.newThread(r);
		newThread.setName(name + "-" + counter);
		counter++;
		return newThread;
	}
};
