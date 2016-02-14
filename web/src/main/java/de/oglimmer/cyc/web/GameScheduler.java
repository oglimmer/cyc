package de.oglimmer.cyc.web;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

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
public enum GameScheduler {
	INSTANCE;

	private static final String CRON_SCHEDULE = "0 0/15 * * * ?";

	private volatile boolean running;
	private volatile Thread thread;

	private volatile Trigger trigger;
	private volatile Scheduler scheduler;

	@Getter
	@Setter
	private String warVersion;

	private GameScheduler() {
		trigger = newTrigger().withIdentity("trigger1", "group1").withSchedule(cronSchedule(CRON_SCHEDULE))
				.forJob("job1", "group1").build();
		
		running = true;
		thread = new Thread(new MasterToStartObserver(), "MasterObserver-" + Math.random());
		thread.start();
	}

	@SneakyThrows(value = { SchedulerException.class })
	public void stop() {
		GlobalGameExecutor.INSTANCE.close();
		if (scheduler != null) {
			scheduler.shutdown(true);
		}
		running = false;
		if (thread != null) {
			thread.interrupt();
		}
	}

	public Date getNextRun() {
		return trigger.getFireTimeAfter(new Date());
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

						scheduler.scheduleJob(job, trigger);

						scheduler.start();
						running = false;
					} else {
						sleep();
					}
				}
			} catch (SchedulerException e) {
				log.error("Failed to start Quartz-scheduler in " + warVersion, e);
			}
			thread = null;
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
