package de.derpeterson.webapp.listener;

import java.io.File;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import de.derpeterson.webapp.config.AppConfigManager;
import de.derpeterson.webapp.helper.LogHelper;
import de.derpeterson.webapp.jobs.JobManager;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import de.derpeterson.webapp.persistence.PersistenceManager;

@WebListener
public class ContextListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContextListener.class);

	public void contextInitialized(ServletContextEvent event) {

		LOGGER.debug("Application will be initialized...");
		
		LogHelper.INSTANCE.tieSystemOutAndErrToLog();

		// Initial ApplicationConfig
		try {	
			File configFile = new File(event.getServletContext().getRealPath("/WEB-INF/app.xml"));
			AppConfigManager appConfigManager = AppConfigManager.INSTANCE.load(configFile);
			if(Objects.nonNull(appConfigManager)) {
				LOGGER.debug("AppConfigManager initialized");
			}
			//			try {
			//				ApplicationConfig appConfig = new ApplicationConfig();
			//				File configFile = new File(event.getServletContext().getRealPath("/WEB-INF/app.xml"));
			//				JAXB.marshal(appConfig, configFile);
			//			} catch(Exception ex) {
			//				ex.printStackTrace();
			//			}
		} catch(Exception e) {
			LOGGER.error("Initialized AppConfigManager failed", e);
		} finally {
		}	

		// Initial EntityManager
		try {
			EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
			if(Objects.nonNull(entityManager)) {
				LOGGER.debug("PersistenceManager initialized");
				//				User user = new User();
				//				user.setEmail("c.doebl@gmx.de").setUsername("derPeterson").setPassword(PasswordHelper.hashPassword("Malissa"));
				//				entityManager.getTransaction().begin();
				//				entityManager.persist(user);
				//				entityManager.getTransaction().commit();
				//				LOGGER.info("update default user");
			}
		} catch(Exception e) {
			LOGGER.error("Initialize PersistenceManager failed", e);
		}
		
	

		try {
			JobManager.INSTANCE.registerDefaultJobs();
			
			LOGGER.debug("Default Jobs registered");
		} catch (SchedulerException e) {
			LOGGER.error("Register default jobs failed", e);
			
		}
		
		LOGGER.debug("Application initialized");
	}

	public void contextDestroyed(ServletContextEvent event) {
		try {
			PersistenceManager.INSTANCE.close();
			
			 // This manually deregisters JDBC driver, which prevents Tomcat 7 from complaining about memory leaks wrto this class
	        Enumeration<Driver> drivers = DriverManager.getDrivers();
	        while (drivers.hasMoreElements()) {
	            Driver driver = drivers.nextElement();
	            try {
	                DriverManager.deregisterDriver(driver);
	                LOGGER.debug("Deregistered jdbc driver: {}", driver);
	            } catch (SQLException e) {
	            	LOGGER.error("Error deregistering driver {}", driver, e);
	            }

	        }
	        
	        try {
	            AbandonedConnectionCleanupThread.shutdown();

	            LOGGER.debug("AbandonedConnectionCleanupThread shutdowned");
	        } catch (InterruptedException e) {
	        	LOGGER.error("AbandonedConnectionCleanupThread shutdown failed", e);
	            e.printStackTrace();
	        }
	        
			LOGGER.debug("PersistenceManager closed");
		} catch(Exception e) {
			LOGGER.error("PersistenceManager close failed", e);
		}
		
		try {
			JobManager.INSTANCE.shutdown();
			
			LOGGER.debug("Default Jobs shutdowned");
		} catch(Exception e) {
			LOGGER.error("Default Jobs shutdown failed", e);
		}
	}
}