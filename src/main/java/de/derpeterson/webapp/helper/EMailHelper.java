package de.derpeterson.webapp.helper;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.omnifaces.util.Faces;

import de.derpeterson.webapp.config.AppConfigManager;
import de.derpeterson.webapp.constants.KnownPages;
import de.derpeterson.webapp.model.PasswordResetToken;
import de.derpeterson.webapp.model.User;
import de.derpeterson.webapp.model.VerificationToken;
import de.derpeterson.webapp.resources.Messages;

public enum EMailHelper {

	INSTANCE;

	private EMailHelper() {}

	private HtmlEmail createDefaultHtmlEmail(boolean isDebug) throws EmailException, GeneralSecurityException {
		HtmlEmail email = null;

		try {
			email = new HtmlEmail();
			email.setHostName(AppConfigManager.INSTANCE.getEMailConfig().getSmptHost());
			email.setSmtpPort(AppConfigManager.INSTANCE.getEMailConfig().getSmptPort());
			try {
				email.setAuthenticator(new DefaultAuthenticator(AppConfigManager.INSTANCE.getEMailConfig().getUsername(), AppConfigManager.INSTANCE.getEMailConfig().getPassword()));
			} catch (/* GeneralSecurityException */ Exception e) {
				throw e;
			}
			email.setSSLOnConnect(true);
			email.setDebug(isDebug);

			email.setFrom(AppConfigManager.INSTANCE.getEMailConfig().getSupportAddress());
		} catch (/* GeneralSecurityException | */ EmailException e) {
			throw e;
		} finally {
			
		}

		return email;
	}

	public boolean sendSignUpEmail(VerificationToken verificationToken) throws EmailException, GeneralSecurityException, IOException {
		try {
			if(Objects.nonNull(verificationToken) && Objects.nonNull(verificationToken.getUser())) {
				User user = verificationToken.getUser();
				HtmlEmail email = createDefaultHtmlEmail(false);
				email.setSubject(String.format(Messages.getString("email.signup.subject"), AppConfigManager.INSTANCE.getApplicationConfig().getAppName()));
				email.addTo(user.getEmail());
	
				File signUpHtmlFile = new File(Faces.getRealPath("/email/simple.xhtml"));
				String signUpHtmlString = FileUtils.readFileToString(signUpHtmlFile, "UTF-8");
	
				if(StringUtils.isNotBlank(signUpHtmlString)) {
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_WELCOME_TEXT]")) {
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_WELCOME_TEXT]", String.format(Messages.getString("email.signup.welcomeText"), AppConfigManager.INSTANCE.getApplicationConfig().getAppName()));
					}
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_BODY_HEADER_TEXT]")) {
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_BODY_HEADER_TEXT]", String.format(Messages.getString("email.signup.bodyHeaderText"), user.getUsername()));
					}				
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_BODY_TEXT]")) {
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_BODY_TEXT]", Messages.getString("email.signup.bodyText"));
					}
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_BUTTON_LINK]")) {
						String baseUrl = Faces.getRequestBaseURL();
						String verifyEmailUrl = baseUrl + KnownPages.createEmailVerifyUrl(verificationToken.getToken());
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_BUTTON_LINK]", verifyEmailUrl);
					}
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_BUTTON_TEXT]")) {
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_BUTTON_TEXT]", Messages.getString("email.signup.bodyButton"));
					}
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_BODY_FOOTER_TEXT]")) {
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_BODY_FOOTER_TEXT]", String.format(Messages.getString("email.general.bodyFooterText"), AppConfigManager.INSTANCE.getApplicationConfig().getAppName()));
					}
					email.setContent(signUpHtmlString, "text/html; charset=utf-8"); 
					email.send();
					
					return true;
				}
			}
		} catch (EmailException | GeneralSecurityException | IOException e) {
			throw e;
		} finally {

		}
		
		return false;
	}

	public boolean sendResetPasswordEmail(PasswordResetToken passwordResetToken) throws EmailException, GeneralSecurityException, IOException {
		try {
			if(Objects.nonNull(passwordResetToken) && Objects.nonNull(passwordResetToken.getUser())) {
				User user = passwordResetToken.getUser();
				HtmlEmail email = createDefaultHtmlEmail(false);
				email.setSubject(String.format(Messages.getString("email.resetPassword.subject"), AppConfigManager.INSTANCE.getApplicationConfig().getAppName()));
				email.addTo(user.getEmail());
				
				File signUpHtmlFile = new File(Faces.getRealPath("/email/simple.xhtml"));
				String signUpHtmlString = FileUtils.readFileToString(signUpHtmlFile, "UTF-8");

				if(StringUtils.isNotBlank(signUpHtmlString)) {
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_WELCOME_TEXT]")) {
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_WELCOME_TEXT]", String.format(Messages.getString("email.resetPassword.welcomeText"), AppConfigManager.INSTANCE.getApplicationConfig().getAppName()));
					}
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_BODY_HEADER_TEXT]")) {
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_BODY_HEADER_TEXT]", String.format(Messages.getString("email.resetPassword.bodyHeaderText"), user.getUsername()));
					}				
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_BODY_TEXT]")) {
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_BODY_TEXT]", Messages.getString("email.resetPassword.bodyText"));
					}
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_BUTTON_LINK]")) {
						String baseUrl = Faces.getRequestBaseURL();
						String verifyEmailUrl = baseUrl + KnownPages.createEmailChangePasswordUrl(passwordResetToken.getToken());
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_BUTTON_LINK]", verifyEmailUrl);
					}
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_BUTTON_TEXT]")) {
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_BUTTON_TEXT]", Messages.getString("email.resetPassword.bodyButton"));
					}
					if(StringUtils.contains(signUpHtmlString, "[EMAIL_BODY_FOOTER_TEXT]")) {
						String standardFooterText = String.format(Messages.getString("email.general.bodyFooterText"), AppConfigManager.INSTANCE.getApplicationConfig().getAppName());
						String resetPasswordFooterText = Messages.getString("email.resetPassword.bodyFooterText") +  "<br/><br/>" + standardFooterText;
						signUpHtmlString = StringUtils.replace(signUpHtmlString, "[EMAIL_BODY_FOOTER_TEXT]", resetPasswordFooterText);
					}
					email.setContent(signUpHtmlString, "text/html; charset=utf-8"); 
					email.send();
					
					return true;
				}
			}
		} catch (EmailException | GeneralSecurityException | IOException e) {
			throw e;
		} finally {
			
		}
		
		return false;
	}
}

