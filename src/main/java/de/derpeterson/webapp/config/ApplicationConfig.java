package de.derpeterson.webapp.config;

import java.security.GeneralSecurityException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ApplicationConfig {
	
	private String appName = "WebApp";
	
	private EMailConfig emailConfig = null;
	
	private boolean deleteUserWhenVerificationExpired = false;
	
	public ApplicationConfig() throws GeneralSecurityException {
		try {
			this.emailConfig = new EMailConfig();
		} catch(GeneralSecurityException e) {
			throw e;
		}
	}
	
	@XmlElement
	public EMailConfig getEmailConfig() {
		return emailConfig;
	}

	public void setEmailConfig(EMailConfig emailConfig) {
		this.emailConfig = emailConfig;
	}

	@XmlElement
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@XmlElement
	public boolean isDeleteUserWhenVerificationExpired() {
		return deleteUserWhenVerificationExpired;
	}

	public void setDeleteUserWhenVerificationExpired(boolean deleteUserWhenVerificationExpired) {
		this.deleteUserWhenVerificationExpired = deleteUserWhenVerificationExpired;
	}	
	
}