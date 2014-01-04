package de.oglimmer.cyc.web;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

@Slf4j
public enum GameExecutor {
	INSTANCE;

	private static final boolean DEBUG_AUTO_START = false;
	private static final String CRON_SCHEDULE = "0 0/15 * * * ?";

	private volatile boolean running;
	private volatile Thread thread;

	private volatile Trigger trigger;
	private volatile Scheduler scheduler;

	@Getter
	@Setter
	private String warVersion;

	private GameExecutor() {

		trigger = newTrigger().withIdentity("trigger1", "group1").withSchedule(cronSchedule(CRON_SCHEDULE))
				.forJob("job1", "group1").build();

		running = true;
		thread = new Thread(new MasterToStartObserver(), "MasterObserver-" + Math.random());
		thread.start();
	}

	@SneakyThrows(value = { SchedulerException.class, InterruptedException.class })
	public void stop() {
		GlobalGameExecutor.INSTANCE.close();
		if (scheduler != null) {
			scheduler.shutdown(true);
		}
		running = false;
		synchronized (thread) {
			if (thread != null) {
				thread.interrupt();
			}
		}
		TimeUnit.SECONDS.sleep(1);
	}

	public Date getNextRun() {
		return trigger.getFireTimeAfter(new Date());
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

	private synchronized Socket getClientSocket() throws IOException {
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
			String home = System.getProperty("cyc.home");
			assert home != null;

			String[] commandLineArgs = createCommandLineArray(home);

			log.debug(Arrays.toString(commandLineArgs));

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

	private String[] createCommandLineArray(String home) {
		StringBuilder buff = new StringBuilder();
		buff.append("java");
		buff.append(" -Xmx256M");
		buff.append(" -XX:MaxPermSize=256M");
		buff.append(" -Dcyc.home=" + home);
		buff.append(" -Djava.security.policy=" + home + "/security.policy");

		if ("enabled".equalsIgnoreCase(System.getProperty("cyc.remoteDebug"))) {
			buff.append(" -Xdebug");
			buff.append(" -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n");
		}

		if ("enabled".equalsIgnoreCase(System.getProperty("cyc.jmx"))) {
			buff.append(" -Dcom.sun.management.jmxremote.port=9997 -Dcom.sun.management.jmxremote.password.file=jmxremote.password -Dcom.sun.management.jmxremote.ssl=false");
		}

		buff.append(" -jar " + home + "/engine-container-jar-with-dependencies.jar");

		Collection<String> commandLineCol = new ArrayList<>();
		commandLineCol.add("sh");
		commandLineCol.add("-c");
		commandLineCol.add(buff.toString());
		return commandLineCol.toArray(new String[commandLineCol.size()]);
	}

	class MasterToStartObserver implements Runnable {

		@Override
		public void run() {
			log.debug("MasterToStartObserver in {} started", warVersion);
			try {
				while (running) {
					if (GlobalGameExecutor.INSTANCE.isMaster()) {
						log.debug("Starting Quartz-scheduler in {} now", warVersion);

						scheduler = StdSchedulerFactory.getDefaultScheduler();
						JobDetail job = newJob(GameExecutionJob.class).withIdentity("job1", "group1").build();

						trigger = newTrigger().withIdentity("trigger1", "group1")
								.withSchedule(cronSchedule(CRON_SCHEDULE)).forJob("job1", "group1").build();

						scheduler.scheduleJob(job, trigger);

						scheduler.start();
						running = false;
					}
					sleep();
				}
			} catch (SchedulerException e) {
				log.error("Failed to start Quartz-scheduler in " + warVersion, e);
			}
			synchronized (thread) {
				thread = null;
			}
			log.debug("MasterToStartObserver in {} ended", warVersion);
		}

		private void sleep() {
			try {
				if (running) {
					TimeUnit.SECONDS.sleep(15);
				}
			} catch (InterruptedException e) {
				// don't care about this
			}
		}
	}

}
