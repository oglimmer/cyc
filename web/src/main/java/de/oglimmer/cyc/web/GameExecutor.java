package de.oglimmer.cyc.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum GameExecutor {
	INSTANCE;

	private static final String TMP_SECURITY_POLICY = System.getProperty("java.io.tmpdir") + "/security.policy";

	private static Logger log = LoggerFactory.getLogger(GameExecutor.class);

	private final String[] jarDependencies = new String[] { "hamcrest-core-1.3.jar", "js-1.7R2.jar",
			"slf4j-api-1.7.5.jar", "logback-classic-1.0.13.jar", "logback-core-1.0.13.jar", "org.ektorp-1.2.2.jar",
			"jackson-core-asl-1.8.6.jar", "jackson-mapper-asl-1.8.6.jar", "httpclient-4.1.1.jar", "httpcore-4.1.jar",
			"commons-logging-1.1.1.jar", "commons-codec-1.4.jar", "httpclient-cache-4.1.1.jar", "commons-io-2.0.1.jar",
			"groovy-all-2.1.7.jar" };

	private final String base = "/WEB-INF/lib/";

	private final String engineJar = "engine-0.1-SNAPSHOT.jar";
	private final String rulesJar = "rules-0.1-SNAPSHOT.jar";
	private final String persistenceJar = "persistence-0.1-SNAPSHOT.jar";

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

	public void runGame(final String userId) throws IOException {
		Socket clientSocket = null;
		try {
			clientSocket = getClientSocket();
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			if (userId != null) {
				outToServer.writeBytes(userId + '\n');
			} else {
				outToServer.writeBytes("full\n");
			}
			String serverResponse = inFromServer.readLine();
			if (!"ok".equals(serverResponse)) {
				log.error("Call to game server returned error:{}", serverResponse);
				throw new IOException("Server returned:" + serverResponse);
			}

		} finally {
			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException e) {
					log.debug("Failed to close to game-server", e);
				}
			}
		}
	}

	private synchronized Socket getClientSocket() throws UnknownHostException, IOException {
		Socket clientSocket;
		try {
			/* port defined in de.oglimmer.cyc.GameServer.SERVER_PORT */
			clientSocket = new Socket("localhost", 9998);
		} catch (ConnectException e) {
			startServer();
			clientSocket = new Socket("localhost", 9998);
		}
		return clientSocket;
	}

	public void startServer() throws IOException {
		try {
			unzip(rootPath + base + engineJar);
			String[] commandLineArgs = createCommandLineArray();

			log.debug(Arrays.toString(commandLineArgs));

			Files.createDirectories(Paths.get(rootPath + "/WEB-INF/engineLogs"));

			ProcessBuilder pb = new ProcessBuilder(commandLineArgs);
			pb.redirectError(new File(rootPath + "/WEB-INF/engineLogs/engine.err"));
			pb.redirectOutput(new File(rootPath + "/WEB-INF/engineLogs/engine.out"));
			pb.start();

			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
		} catch (IOException e) {
			log.error("Failed to process the child process", e);
			throw e;
		}
	}

	private String[] createCommandLineArray() {
		StringBuilder buff = new StringBuilder();
		buff.append("java");
		buff.append(" -Xms2M");
		buff.append(" -Xmx96M");
		buff.append(" -cp ");

		for (String s : jarDependencies) {
			buff.append(rootPath + base + s + ":");
		}
		buff.append(rootPath + base + engineJar + ":");
		buff.append(rootPath + base + rulesJar + ":");
		buff.append(rootPath + base + persistenceJar + ":");

		buff.append(" -Dcyc.web-inf-lib=" + rootPath + base);
		buff.append(" -Djava.security.policy=" + TMP_SECURITY_POLICY);

		// buff.append(" -Xdebug");
		// buff.append(" -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n");

		buff.append(" ");
		buff.append("de.oglimmer.cyc.GameServer");

		Collection<String> commandLineCol = new ArrayList<>();
		commandLineCol.add("sh");
		commandLineCol.add("-c");
		commandLineCol.add(buff.toString());
		String[] commandLineArgs = commandLineCol.toArray(new String[commandLineCol.size()]);
		return commandLineArgs;
	}

	class Runner implements Runnable {
		@Override
		public void run() {

			while (running) {

				Date now = new Date();

				if (nextRun.before(now)) {
					try {
						runGame(null);
					} catch (IOException e) {
						log.error("Failed to run game", e);
					}
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
