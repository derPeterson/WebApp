package de.derpeterson.webapp.controller;


import java.io.Serializable;
import java.util.Objects;

import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.derpeterson.webapp.bean.LocaleBean;
import de.derpeterson.webapp.bean.MessageBean;
import de.derpeterson.webapp.bean.VerifyBean;
import de.derpeterson.webapp.constants.KnownPages;
import de.derpeterson.webapp.exception.UserPasswordIncorrectlyException;
import de.derpeterson.webapp.form.ChangePasswordFormBean;
import de.derpeterson.webapp.form.LoginFormBean;
import de.derpeterson.webapp.form.ResetPasswordFormBean;
import de.derpeterson.webapp.form.SignUpFormBean;
import de.derpeterson.webapp.helper.EMailHelper;
import de.derpeterson.webapp.helper.PasswordHelper;
import de.derpeterson.webapp.helper.SecurityHelper;
import de.derpeterson.webapp.helper.UsernameHelper;
import de.derpeterson.webapp.model.PasswordResetToken;
import de.derpeterson.webapp.model.User;
import de.derpeterson.webapp.model.VerificationToken;
import de.derpeterson.webapp.persistence.PersistenceManager;
import de.derpeterson.webapp.resources.Messages;

@ManagedBean
@SessionScoped
public class UserManager implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);

	private User user;

	public String login() {			
		MessageBean messageBean = MessageBean.getInstanceInFacesContext();
		messageBean.setLoginFailMessage(Messages.getString("login.loginFailedMessage"));
		
		// Lookup the user based on user name and user password
		EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

		LoginFormBean loginFormBean = LoginFormBean.getInstanceInFacesContext();

		User result = null;
		try {
			result = User.findByUsernameOrEMailWithPassword(entityManager, loginFormBean.getUsernameOrEmail(), loginFormBean.getUserPassword());
		} catch (UserPasswordIncorrectlyException e) {
			// Kein Fehler in dem Sinn
		}

		if(Objects.nonNull(result)) {
			this.user = result;

			LOGGER.info("Login successful for '{}({})'", result.getUsername(), result.getEmail());

			RequestContext.getCurrentInstance().addCallbackParam("loggedIn", true);

			return KnownPages.HOME_PAGE;
		} else {
			LOGGER.info("Login failed for '{}'", loginFormBean.getUsernameOrEmail());
			messageBean.setLoginFailMessage(Messages.getString("login.loginFailedMessage"));

			RequestContext.getCurrentInstance().addCallbackParam("loggedIn", false);

			return null;
		}
	}

	public String signup() {		
		SignUpFormBean signUpFormBean = SignUpFormBean.getInstanceInFacesContext();

		MessageBean messageBean = MessageBean.getInstanceInFacesContext();

		messageBean.setSignUpUsernameFailMessage("");

		EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

		// Variablen abspeichern

		String username = signUpFormBean.getUsername();
		String email = signUpFormBean.getEmail();
		String emailToConfirm = signUpFormBean.getEmailToConfirm();
		String password = signUpFormBean.getPassword();

		//////////////////////////
		// USERNAME 			//
		//////////////////////////

		// Username leer?
		if(StringUtils.isBlank(username)) {
			RequestContext.getCurrentInstance().addCallbackParam("usernameValid", false);
			messageBean.setSignUpUsernameFailMessage(Messages.getString("signup.usernameEmptyFailedMessage"));
			return null;
		} else
			// Username zu kurz?
			if(StringUtils.length(username) < SignUpFormBean.minimumUsernameLength) {
				RequestContext.getCurrentInstance().addCallbackParam("usernameValid", false);
				messageBean.setSignUpUsernameFailMessage(String.format(Messages.getString("signup.usernameToShortFailedMessage"), SignUpFormBean.minimumUsernameLength));
				return null;
			} else
				// Username zu lang?
				if(StringUtils.length(username) > SignUpFormBean.maximumUsernameLength) {
					RequestContext.getCurrentInstance().addCallbackParam("usernameValid", false);
					messageBean.setSignUpUsernameFailMessage(String.format(Messages.getString("signup.usernameToLongFailedMessage"), SignUpFormBean.maximumUsernameLength));
					return null;
				} else
					// Username invalid?
					if(!UsernameHelper.INSTANCE.validate(username)) {
						RequestContext.getCurrentInstance().addCallbackParam("usernameValid", false);
						messageBean.setSignUpUsernameFailMessage(Messages.getString("signup.usernameNotValidFailedMessage"));
						return null;
					} else
						// Username available?
						if(Objects.nonNull(User.findByUsername(entityManager, username))) {
							RequestContext.getCurrentInstance().addCallbackParam("usernameValid", false);
							messageBean.setSignUpUsernameFailMessage(Messages.getString("signup.usernameNotAvailableFailedMessage"));
							return null;
						} else {
							RequestContext.getCurrentInstance().addCallbackParam("usernameValid", true);
						}

		//////////////////////////
		// EMAIL     			//
		//////////////////////////

		// EMail leer?
		if(StringUtils.isBlank(email)) {
			RequestContext.getCurrentInstance().addCallbackParam("emailValid", false);
			messageBean.setSignUpUsernameFailMessage(Messages.getString("signup.emailEmptyFailedMessage"));
			return null;
		} else
			// EMail invalid?
			if(!EmailValidator.getInstance().isValid(email)) {
				RequestContext.getCurrentInstance().addCallbackParam("emailValid", false);
				messageBean.setSignUpUsernameFailMessage(Messages.getString("signup.emailNotValidFailedMessage"));
				return null;
			} else
				// EMail Confirm?
				if(!StringUtils.equals(email, emailToConfirm)) {
					RequestContext.getCurrentInstance().addCallbackParam("emailValid", false);
					messageBean.setSignUpUsernameFailMessage(Messages.getString("signup.emailToConfirmMatchNotFailedMessage"));
					return null;
				} else 
					// EMail available
					if(Objects.nonNull(User.findByEMail(entityManager, email))) {
						RequestContext.getCurrentInstance().addCallbackParam("emailValid", false);
						messageBean.setSignUpUsernameFailMessage(Messages.getString("signup.emailNotAvailableFailedMessage"));
						return null;
					} else {
						RequestContext.getCurrentInstance().addCallbackParam("emailValid", true);
					}

		//////////////////////////
		// PASSWORD 			//
		//////////////////////////

		// Password leer?
		if(StringUtils.isBlank(password)) {
			RequestContext.getCurrentInstance().addCallbackParam("passwordValid", false);
			messageBean.setSignUpUsernameFailMessage(Messages.getString("signup.passwordEmptyFailedMessage"));
			return null;
		} else
			// Password zu kurz?
			if(StringUtils.length(password) < SignUpFormBean.minimumPasswordLength) {
				RequestContext.getCurrentInstance().addCallbackParam("passwordValid", false);
				messageBean.setSignUpUsernameFailMessage(String.format(Messages.getString("signup.passwordToShortFailedMessage"), SignUpFormBean.minimumPasswordLength));
				return null;
			} else
				// Password zu lang?
				if(StringUtils.length(password) > SignUpFormBean.maximumPasswordLength) {
					RequestContext.getCurrentInstance().addCallbackParam("passwordValid", false);
					messageBean.setSignUpUsernameFailMessage(String.format(Messages.getString("signup.passwordToLongFailedMessage"), SignUpFormBean.maximumPasswordLength));
					return null;
				} else {
					RequestContext.getCurrentInstance().addCallbackParam("passwordValid", true);
				}

		try {
			String newPassword = PasswordHelper.hashPassword(password);		
			User newUser = new User().setEmail(email).setUsername(username).setPassword(newPassword);

			entityManager.getTransaction().begin();
			
			entityManager.persist(newUser);		

			VerificationToken verificationToken = new VerificationToken().setToken(SecurityHelper.INSTANCE.generateUserToken(true, newUser)).setUser(newUser);
			
			entityManager.persist(verificationToken);
			
			if(EMailHelper.INSTANCE.sendSignUpEmail(verificationToken)) {
				this.user = newUser;
				
				entityManager.getTransaction().commit();

				LOGGER.info("New user '{}({})' registered in database and activating e-mail has been send", this.user.getUsername(), this.user.getEmail());

				return KnownPages.HOME_PAGE;
			} else {
				throw new Exception("Send signup email failed");
			}
		} catch (Exception e) {
			LOGGER.error("Registration failed for '{}({})'", username, email, e);

			entityManager.getTransaction().rollback();

			messageBean.setSignUpUsernameFailMessage(Messages.getString("error.internFailedMessage"));
			return null;
		} finally {

		}
	}

	// Zu Testzwecken angelegt
	public void changeLanguage() {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		LocaleBean localBean = (LocaleBean) elContext.getELResolver().getValue(elContext, null, "localeBean");
		localBean.setLanguage("en", true);
	}

	public String verify() {
		try {
			VerifyBean verifyBean = VerifyBean.getInstanceInFacesContext();

			EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

			String verificationKey = verifyBean.getVerificationKey();
			VerificationToken verificationToken = null;
			if(StringUtils.isNotBlank(verificationKey)) {
				verificationToken = VerificationToken.findByToken(entityManager, verificationKey);
			}

			if(Objects.nonNull(verificationToken)) {				
				User userToVerification = verificationToken.getUser();
				
				userToVerification.setEnabled(true);
				userToVerification.setVerificationToken(null);
				
				entityManager.getTransaction().begin();
				entityManager.persist(userToVerification);
				entityManager.remove(verificationToken);
				
				entityManager.getTransaction().commit();
				
				this.user = userToVerification;

				LOGGER.info("User '{}({})' has successfully verified email", user.getUsername(), user.getEmail());
			} else {
				LOGGER.info("Verify email failed '{}'", verificationKey);
			}			
		} catch(Exception e) {
			LOGGER.error("Verify email failed", e);
		}

		return isLoggedIn() ? KnownPages.HOME_PAGE : KnownPages.LOGIN_PAGE;
	}

	public String resetPassword() {		
		ResetPasswordFormBean resetPasswordFormBean = ResetPasswordFormBean.getInstanceInFacesContext();

		MessageBean messageBean = MessageBean.getInstanceInFacesContext();

		messageBean.setSignUpUsernameFailMessage("");

		EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

		String usernameOrEmail = resetPasswordFormBean.getUsernameOrEmail();

		if(StringUtils.isBlank(usernameOrEmail)) {
			RequestContext.getCurrentInstance().addCallbackParam("usernameOrEmailValid", false);
			messageBean.setResetPasswordMessage(Messages.getString("resetPassword.usernameOrEmailEmptyFailedMessage"));
			return null;
		} else {
			RequestContext.getCurrentInstance().addCallbackParam("usernameOrEmailValid", true);
		}
		
		User user = User.findByUsernameOrEMail(entityManager, usernameOrEmail);

		if(Objects.nonNull(user)) {
			try {
				String newToken = SecurityHelper.INSTANCE.generateUserToken(true, user);
				PasswordResetToken resetPasswordToken = user.getPasswordResetToken();
				if(Objects.isNull(resetPasswordToken)) {
					resetPasswordToken = new PasswordResetToken().setToken(newToken).setUser(user);
				} else {
					resetPasswordToken.setToken(newToken).calculateExpirationDate();
				}
				
				entityManager.getTransaction().begin();
				entityManager.persist(resetPasswordToken);

				if(EMailHelper.INSTANCE.sendResetPasswordEmail(resetPasswordToken)) {						
					entityManager.getTransaction().commit();

					LOGGER.info("User '{}({})' reset password and e-mail has been send", user.getUsername(), user.getEmail());

					RequestContext.getCurrentInstance().addCallbackParam("restPassword", true);
					messageBean.setResetPasswordMessage(Messages.getString("resetPassword.successMessage"));
				} else {
					throw new Exception("Send reset password email failed");
				}
			} catch (Exception e) {
				LOGGER.error("Reset Password failed for '{}({})'", user.getUsername(), user.getEmail(), e);

				entityManager.getTransaction().rollback();

				RequestContext.getCurrentInstance().addCallbackParam("restPassword", false);
				messageBean.setResetPasswordMessage(Messages.getString("error.internFailedMessage"));
				return null;
			} 
		} else {
			RequestContext.getCurrentInstance().addCallbackParam("restPassword", false);
			messageBean.setResetPasswordMessage(Messages.getString("resetPassword.usernameOrEmailNotFoundFailedMessage"));
			return null;
		}

		return null;
	}

	public String validatePasswordResetToken() {
		EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

		String passwordResetTokenString = ChangePasswordFormBean.getInstanceInFacesContext().getPasswordResetToken();

		PasswordResetToken passwordResetToken = null;
		
		if(StringUtils.isBlank(passwordResetTokenString)
				|| Objects.isNull(passwordResetToken = PasswordResetToken.findByToken(entityManager, passwordResetTokenString))
				|| passwordResetToken.isExpired()) {
			LOGGER.info("Check password reset token failed '{}'", passwordResetTokenString);

			return KnownPages.LOGIN_PAGE;
		}

		return null;
	}

	public String changePassword() {		
		ChangePasswordFormBean changePasswordFormBean = ChangePasswordFormBean.getInstanceInFacesContext();

		MessageBean messageBean = MessageBean.getInstanceInFacesContext();

		messageBean.setChangePasswordMessage("");

		EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

		// Variablen abspeichern		
		String newPassword = changePasswordFormBean.getNewPassword();
		String newPasswordToConfirm = changePasswordFormBean.getNewPasswordToConfirm();

		//////////////////////////
		// PASSWORD 			//
		//////////////////////////

		// Password leer?
		if(StringUtils.isBlank(newPassword)) {
			RequestContext.getCurrentInstance().addCallbackParam("passwordValid", false);
			messageBean.setChangePasswordMessage(Messages.getString("changePassword.passwordEmptyFailedMessage"));
			return null;
		} else
			// Password zu kurz?
			if(StringUtils.length(newPassword) < SignUpFormBean.minimumPasswordLength) {
				RequestContext.getCurrentInstance().addCallbackParam("passwordValid", false);
				messageBean.setChangePasswordMessage(String.format(Messages.getString("changePassword.passwordToShortFailedMessage"), SignUpFormBean.minimumPasswordLength));
				return null;
			} else
				// Password zu lang?
				if(StringUtils.length(newPassword) > SignUpFormBean.maximumPasswordLength) {
					RequestContext.getCurrentInstance().addCallbackParam("passwordValid", false);
					messageBean.setChangePasswordMessage(String.format(Messages.getString("changePassword.passwordToLongFailedMessage"), SignUpFormBean.maximumPasswordLength));
					return null;
				} else
					// Password Confirm?
					if(!StringUtils.equals(newPassword, newPasswordToConfirm)) {
						RequestContext.getCurrentInstance().addCallbackParam("passwordValid", false);
						messageBean.setChangePasswordMessage(Messages.getString("changePassword.passwordToConfirmMatchNotFailedMessage"));
						return null;
					} else {
						RequestContext.getCurrentInstance().addCallbackParam("passwordValid", true);
					}

		String passwordResetTokenString = changePasswordFormBean.getPasswordResetToken();

		PasswordResetToken passwordResetToken = null;
		
		if(StringUtils.isBlank(passwordResetTokenString)
				|| Objects.isNull(passwordResetToken = PasswordResetToken.findByToken(entityManager, passwordResetTokenString))
				|| passwordResetToken.isExpired()) {			
			LOGGER.debug("Change password failed 'passwordResetToken={}'", passwordResetTokenString);
			
			RequestContext.getCurrentInstance().addCallbackParam("changePassword", false);
			messageBean.setChangePasswordMessage(Messages.getString("changePassword.resetPasswordTokenInvalidFailedMessage"));
			return null;
		}
		
		try {
			User userToChangePassword = passwordResetToken.getUser();
			userToChangePassword.setPasswordResetToken(null);

			String newPasswordHash = PasswordHelper.hashPassword(newPassword);		
			userToChangePassword.setPassword(newPasswordHash);

			entityManager.getTransaction().begin();
			entityManager.remove(passwordResetToken);
			entityManager.persist(userToChangePassword);
			
			entityManager.getTransaction().commit();		

			LOGGER.info("User '{}({})' changed password", userToChangePassword.getUsername(), userToChangePassword.getEmail());

			RequestContext.getCurrentInstance().addCallbackParam("changePassword", true);
			messageBean.setChangePasswordMessage(Messages.getString("changePassword.successMessage"));	
		} catch (Exception e) {
			LOGGER.error("Change Password failed for '{}({})'", user.getUsername(), user.getEmail(), e);

			entityManager.getTransaction().rollback();

			RequestContext.getCurrentInstance().addCallbackParam("changePassword", false);
			messageBean.setChangePasswordMessage(Messages.getString("error.internFailedMessage"));
		} 
		
		return null;
	}

	public String logout() {
		if(Objects.isNull(user)) {
			LOGGER.error("No user exists in session");
			return KnownPages.LOGIN_PAGE;
		}

		// invalidate the session
		LOGGER.debug("Invalidating session for '{}({})'", user.getUsername(), user.getEmail());;
		Faces.getSession().invalidate();

		LOGGER.info("Logout successful for '{}({})'", user.getUsername(), user.getEmail());;

		return KnownPages.LOGIN_PAGE;
	}

	public boolean isLoggedIn() {
		return user != null;
	}

	public String isLoggedInForwardHome() {
		if (isLoggedIn()) {
			return KnownPages.HOME_PAGE;
		}

		return null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User currentUser) {
		this.user = currentUser;
	}
}