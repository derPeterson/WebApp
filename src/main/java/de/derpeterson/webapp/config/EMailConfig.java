package de.derpeterson.webapp.config;

import java.security.GeneralSecurityException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.derpeterson.webapp.helper.SecurityHelper;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.PROPERTY)
public class EMailConfig {
	
	private String username = "user@gmail.com";
	
	private String password = "password";
	
	private String smptHost = "smtp.gmail.com";
	
	private Integer smptPort = 465;
	
	private String supportAddress = "support@webapp.com";
	
	public EMailConfig() throws GeneralSecurityException {
//	 	Unhandlich, erstmal mit Klartext arbeiten
//		try {
//			this.password = SecurityHelper.INSTANCE.encrypt("password");
//		} catch(GeneralSecurityException e) {
//			throw e;
//		}
	}

	@XmlElement
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlElement
	public String getPassword() {
		return password;
	}

// 	Unhandlich, erstmal mit Klartext arbeiten
//	public String getPasswordDecrypted() throws GeneralSecurityException {
//		try {
//			return SecurityHelper.INSTANCE.decrypt(this.password);
//		} catch (GeneralSecurityException e) {
//			throw e;
//		}
//	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement
	public String getSmptHost() {
		return smptHost;
	}

	public void setSmptHost(String smptHost) {
		this.smptHost = smptHost;
	}

	@XmlElement
	public Integer getSmptPort() {
		return smptPort;
	}

	public void setSmptPort(Integer smptPort) {
		this.smptPort = smptPort;
	}

	@XmlElement
	public String getSupportAddress() {
		return supportAddress;
	}

	public void setSupportAddress(String supportAddress) {
		this.supportAddress = supportAddress;
	}
}