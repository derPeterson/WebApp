package de.derpeterson.webapp.jobs;

import java.util.Objects;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public enum JobManager {

	INSTANCE;		
	
	@SuppressWarnings("unused")
	private final Integer triggerIntervalInMinutes = 3;
	
	// Jeden Tag um 3 Uhr Nachts
	private final String triggerIntervalCron = "0 0 3 ? * *";

	private Scheduler scheduler;

	private JobDetail cleanUpDatabaseJob;

	private Trigger databaseTrigger;

	public void registerDefaultJobs() throws SchedulerException {	
		try {
			// Build CleanUpDatabaseJob
			this.cleanUpDatabaseJob = buildCleanUpDatabaseJob();

			// Build Database Trigger
			this.databaseTrigger =  buildDatabaseTrigger();

			// Schedule it
			getScheduler().start();
			getScheduler().scheduleJob(cleanUpDatabaseJob, databaseTrigger);
		} catch (SchedulerException e) {
			throw e;
		} finally {

		}
	}

	private JobDetail buildCleanUpDatabaseJob() {
		return JobBuilder.newJob(CleanUpDatabaseJob.class)
				.withIdentity("cleanUpDatabseJob1", "databaseGroup1")
				.build();
	}

	private Trigger buildDatabaseTrigger() {
		return TriggerBuilder.newTrigger()
				.withIdentity("databaseTrigger1", "databaseGroup1")
				.startNow()
				// SimpleTrigger
//				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
//						.withIntervalInHours(triggerIntervalInMinutes)
//						.repeatForever())
				// CronTrigger
				.withSchedule(CronScheduleBuilder.cronSchedule(triggerIntervalCron))
				.build();
	}

	public JobDetail getCleanUpDatabaseJob() {
		return cleanUpDatabaseJob;
	}

	public Scheduler getScheduler() throws SchedulerException {
		if(Objects.isNull(scheduler)) {
			try {
				scheduler = new StdSchedulerFactory().getScheduler();
			} catch (SchedulerException e) {
				throw e;
			}
		}

		return scheduler;
	}
	
	public void shutdown() throws SchedulerException {
		if(Objects.nonNull(scheduler)) {
			try {
				scheduler.shutdown(true);
			} catch (SchedulerException e) {
				throw e;
			}
		}
	}
}