package de.derpeterson.webapp.persistence;

import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public enum PersistenceManager {
	
	INSTANCE;
	
	private EntityManagerFactory emFactory;

	private PersistenceManager() {
		emFactory = Persistence.createEntityManagerFactory("webapp");
	}

	public EntityManager getEntityManager() {
		if(Objects.nonNull(emFactory)) {
			return emFactory.createEntityManager();
		}
		return null;		
	}

	public void close() {
		if(Objects.nonNull(emFactory)) {
			emFactory.close();
		}
	}
}