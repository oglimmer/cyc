package de.oglimmer.cyc.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum WebGameEngineStarter {
	INSTANCE;

	private static final boolean DEBUG_AUTO_START = false;

	public void startServer() throws IOException {
		if (isServiceAvailable()) {
			serviceStart();
		}
		if (isRunScriptAvailable()) {
			runScriptStart();
		} else {
			directStart();
		}
	}

	private boolean isRunScriptAvailable() {
		return new File(getCycHome() + "/run.sh").exists();
	}

	private void runScriptStart() throws IOException {
		String home = getCycHome();
		log.debug("startEngineProcess via run.sh");
		String[] commandLineArgs = new String[] { home + "/run.sh" };
		createProcess(commandLineArgs, home);
	}

	private void serviceStart() throws IOException {
		startEngineProcess(new String[] { "service", "cyc-engine-container", "start" });
	}

	private boolean isServiceAvailable() {
		return new File("/etc/init.d/cyc-engine-container").exists();
	}

	private void directStart() throws IOException {
		String[] commandLineArgs = createCommandLineArray();
		startEngineProcess(commandLineArgs);
	}

	private void startEngineProcess(String[] commandLineArgs) throws IOException {
		String home = getCycHome();
		log.debug("startEngineProcess: {}", Arrays.toString(commandLineArgs));
		createProcess(commandLineArgs, home);
	}

	private void createProcess(String[] commandLineArgs, String home) throws IOException {
		try {
			ProcessBuilder pb = new ProcessBuilder(commandLineArgs);
			if (DEBUG_AUTO_START) {
				pb.inheritIO();
			} else {
				pb.redirectError(new File(home + "/logs/engine.err"));
				pb.redirectOutput(new File(home + "/logs/engine.out"));
			}
			pb.start();

			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			// ignore if interrupted
		} catch (IOException e) {
			log.error("Failed to process the child process", e);
			throw e;
		}
	}

	private String getCycHome() {
		String home = System.getProperty("cyc.home");
		assert home != null;
		return home;
	}

	private String[] createCommandLineArray() {
		String home = getCycHome();

		StringBuilder buff = new StringBuilder();
		buff.append("java");
		buff.append(" -Xmx256M");
		if (getVersion() < 1.8) {
			buff.append(" -XX:MaxPermSize=256M");
		}
		buff.append(" -Dcyc.home=" + home);
		buff.append(" -Djava.security.policy=" + home + "/security.policy");

		if ("enabled".equalsIgnoreCase(System.getProperty("cyc.remoteDebug"))) {
			buff.append(" -Xdebug");
			buff.append(" -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n");
		}

		if ("enabled".equalsIgnoreCase(System.getProperty("cyc.jmx"))) {
			buff.append(
					" -Dcom.sun.management.jmxremote.port=9997 -Dcom.sun.management.jmxremote.password.file=jmxremote.password -Dcom.sun.management.jmxremote.ssl=false");
		}

		buff.append(" -jar " + home + "/engine-container-jar-with-dependencies.jar");

		Collection<String> commandLineCol = new ArrayList<>();
		commandLineCol.add("sh");
		commandLineCol.add("-c");
		commandLineCol.add(buff.toString());
		return commandLineCol.toArray(new String[commandLineCol.size()]);
	}

	private double getVersion() {
		String version = System.getProperty("java.version");
		int pos = version.indexOf('.');
		pos = version.indexOf('.', pos + 1);
		return Double.parseDouble(version.substring(0, pos));
	}

}
