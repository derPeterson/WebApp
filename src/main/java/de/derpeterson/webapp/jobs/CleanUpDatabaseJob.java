package de.derpeterson.webapp.jobs;

import java.util.Date;

import javax.persistence.EntityManager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.derpeterson.webapp.config.AppConfigManager;
import de.derpeterson.webapp.model.PasswordResetToken;
import de.derpeterson.webapp.model.VerificationToken;
import de.derpeterson.webapp.persistence.PersistenceManager;

public class CleanUpDatabaseJob implements Job {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CleanUpDatabaseJob.class);

	public CleanUpDatabaseJob() {}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.debug("Starting CleanUpDatabaseJob...");
		
		EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
			
		long currentTimestamp = new Date().getTime();
		try {
			Integer deleted = PasswordResetToken.deleteAllOldToken(entityManager, currentTimestamp);
			LOGGER.debug("CleanUpDatabaseJob - PasswordResetToken deleted tokens '{}'", deleted);
		} catch (Exception e) {
			LOGGER.error("CleanUpDatabaseJob - PasswordResetToken cleanup failed", e);
		}
		
		try {
			if(AppConfigManager.INSTANCE.getApplicationConfig().isDeleteUserWhenVerificationExpired()) {
				Integer deleted = VerificationToken.deleteAllUserWithOldToken(entityManager, currentTimestamp);
				LOGGER.debug("CleanUpDatabaseJob - Deleted users '{}' verification expired" , deleted);
			} else {
				Integer deleted = VerificationToken.deleteAllOldToken(entityManager, currentTimestamp);
				LOGGER.debug("CleanUpDatabaseJob - VerificationToken deleted tokens '{}'", deleted);
			}
		
		} catch (Exception e) {
			LOGGER.error("CleanUpDatabaseJob - VerificationToken cleanup failed", e);
		}
		
		LOGGER.debug("CleanUpDatabaseJob finished");
	}
}