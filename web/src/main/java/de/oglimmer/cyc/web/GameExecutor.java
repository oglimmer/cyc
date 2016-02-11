package de.oglimmer.cyc.web;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum GameExecutor {
	INSTANCE;

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

	public void runGame(String userId) throws IOException {
		Socket clientSocket = null;
		try {
			clientSocket = getClientSocket();
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			if (userId == null) {
				userId = "full";
			}
			outToServer.writeBytes(userId + '\n');
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
		try {
			return new Socket(WebContainerProperties.INSTANCE.getEngineHost(),
					WebContainerProperties.INSTANCE.getEnginePort());
		} catch (ConnectException e) {
			return startNewServerAndCreateSocket(e);
		}
	}

	private Socket startNewServerAndCreateSocket(ConnectException e) throws IOException, UnknownHostException {
		if (isLocalEngine()) {
			WebGameEngineStarter.INSTANCE.startServer();
			return connectToStartingEngine();
		} else {
			throw new RuntimeException(e);
		}
	}

	private Socket connectToStartingEngine() throws IOException, ConnectException {
		int retries = 0;
		while (true) {
			try {
				return new Socket(WebContainerProperties.INSTANCE.getEngineHost(),
						WebContainerProperties.INSTANCE.getEnginePort());
			} catch (ConnectException ce) {
				retries++;
				if (retries > 30) {
					throw ce;
				}
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					throw new ConnectException("CONNECT ABORTED DUE TO INTERRUPT EXCEPTION");
				}
			}
		}
	}

	private boolean isLocalEngine() {
		String engineHost = WebContainerProperties.INSTANCE.getEngineHost();
		return "localhost".equalsIgnoreCase(engineHost) || "127.0.0.1".equals(engineHost);
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
