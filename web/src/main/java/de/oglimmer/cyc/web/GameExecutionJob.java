package de.oglimmer.cyc.web;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class GameExecutionJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			GameExecutor.INSTANCE.startFullRun();
		} catch (IOException e) {
			log.error("Failed to run game", e);
		}
	}

}
