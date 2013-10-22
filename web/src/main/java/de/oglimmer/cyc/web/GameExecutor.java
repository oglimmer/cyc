package de.oglimmer.cyc.web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.cyc.GameApp;

public enum GameExecutor {
	INSTANCE;

	private static final String TMP_SECURITY_POLICY = "/tmp/security.policy";

	private static Logger log = LoggerFactory.getLogger(GameExecutor.class);

	private final String[] jarDependencies = new String[] { "hamcrest-core-1.3.jar", "js-1.7R2.jar",
			"slf4j-api-1.7.5.jar", "logback-classic-1.0.13.jar", "logback-core-1.0.13.jar", "org.ektorp-1.2.2.jar",
			"jackson-core-asl-1.8.6.jar", "jackson-mapper-asl-1.8.6.jar", "httpclient-4.1.1.jar", "httpcore-4.1.jar",
			"commons-logging-1.1.1.jar", "commons-codec-1.4.jar", "httpclient-cache-4.1.1.jar", "commons-io-2.0.1.jar",
			"groovy-all-2.1.7.jar" };

	private final String base = "/WEB-INF/lib/";

	private final String engineJar = "engine-0.1-SNAPSHOT.jar";
	private final String rulesJar = "rules-0.1-SNAPSHOT.jar";

	private static final int RUN_EVERY_MINUTES = 15;

	private boolean running;
	private Date nextRun = new Date();
	private Runner runner = new Runner();
	private Thread thread;
	private String rootPath;

	private GameExecutor() {

		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.MINUTE, RUN_EVERY_MINUTES);
		nextRun = cal.getTime();

		running = true;
		thread = new Thread(runner);
		thread.start();
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public Date getNextRun() {
		return nextRun;
	}

	public void stop() {
		running = false;
		thread.interrupt();
	}

	private void unzip(String zipFile) throws IOException {
		byte[] buffer = new byte[1024];
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				if ("security.policy".equals(fileName)) {
					try (FileOutputStream fos = new FileOutputStream(TMP_SECURITY_POLICY)) {
						int len;
						while ((len = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
					}
				}
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
		}
	}

	public String runGame(String userId) {
		StringBuilder buff = new StringBuilder(10240);
		try {
			unzip(rootPath + base + engineJar);
			String[] commandLineArgs = createCommandLineArray(userId, buff);

			Process p = Runtime.getRuntime().exec(commandLineArgs);

			buff = new StringBuilder();
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = br.readLine()) != null) {
				buff.append(line).append("<br/>");
			}
			br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((line = br.readLine()) != null) {
				buff.append(line).append("<br/>");
			}
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			log.error("Failed to process the child process", e);
		}

		return buff.toString();
	}

	private String[] createCommandLineArray(String userId, StringBuilder buff) {
		Collection<String> commandLineCol = new ArrayList<>();
		commandLineCol.add("java");
		commandLineCol.add("-Xms2M");
		commandLineCol.add("-Xmx96M");
		commandLineCol.add("-cp");

		for (String s : jarDependencies) {
			buff.append(rootPath + base + s + ":");
		}
		buff.append(rootPath + base + engineJar + ":");
		buff.append(rootPath + base + rulesJar + ":");

		commandLineCol.add(buff.toString());

		commandLineCol.add("-Dcyc.web-inf-lib=" + rootPath + base);
		commandLineCol.add("-Djava.security.policy=" + TMP_SECURITY_POLICY);
		commandLineCol.add(GameApp.class.getName());
		if (userId != null) {
			commandLineCol.add(userId);
		}

		String[] commandLineArgs = commandLineCol.toArray(new String[commandLineCol.size()]);
		return commandLineArgs;
	}

	class Runner implements Runnable {
		@Override
		public void run() {

			while (running) {

				Date now = new Date();

				if (nextRun.before(now)) {
					log.error(runGame(null));
					Calendar cal = GregorianCalendar.getInstance();
					cal.add(Calendar.MINUTE, RUN_EVERY_MINUTES);
					nextRun = cal.getTime();
				}

				try {
					TimeUnit.SECONDS.sleep(15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
