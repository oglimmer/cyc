package de.oglimmer.cyc.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum GameExecutor {
	INSTANCE;

	private static Logger log = LoggerFactory.getLogger(GameExecutor.class);

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
		String home = System.getProperty("cyc.home");
		assert home != null;
		StringBuilder buff = new StringBuilder();
		buff.append("java");
		buff.append(" -Xms2M");
		buff.append(" -Xmx96M");
		buff.append(" -Dcyc.home=" + home);
		buff.append(" -Djava.security.policy=" + home + "/security.policy");

		// buff.append(" -Xdebug");
		// buff.append(" -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n");

		buff.append(" -jar " + home + "/engine-container-0.1-SNAPSHOT-jar-with-dependencies.jar");

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
					// don't care about this
				}
			}

		}
	}

}
