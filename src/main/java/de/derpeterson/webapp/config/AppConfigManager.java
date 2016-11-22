package de.derpeterson.webapp.config;

import java.io.File;
import java.util.Objects;

import javax.xml.bind.JAXB;

public enum AppConfigManager {
	
	INSTANCE;
	
	private ApplicationConfig appConfig = null;
	
	private File appConfigFile = null;

	private AppConfigManager() {}

	public AppConfigManager load(File configFile) throws Exception {
		try {
			this.appConfigFile = configFile;
			this.appConfig = JAXB.unmarshal(configFile, ApplicationConfig.class);
			if(Objects.nonNull(this.appConfig)) {
				return this;
			} 
		} catch(Exception e) {
			throw e;
		} finally {
			
		}		
		
		return null;
	}

	public void save() throws Exception {
		try {
			JAXB.marshal(this.appConfig, this.appConfigFile);
		} catch(Exception e) {
			throw e;
		} finally {
			
		}		
	}
	
	public ApplicationConfig getApplicationConfig() {
		return this.appConfig;
	}
	
	public File getApplicationConfigFile() {
		return this.appConfigFile;
	}
	
	public EMailConfig getEMailConfig() {
		if(Objects.nonNull(this.appConfig)) {
			return this.appConfig.getEmailConfig();
		}
		return null;
	}
}