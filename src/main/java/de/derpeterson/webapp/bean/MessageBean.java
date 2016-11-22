package de.derpeterson.webapp.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class MessageBean {
	
    public static MessageBean getInstanceInFacesContext() {
        return getInstanceInFacesContext(null);
    }
    
    public static MessageBean getInstanceInFacesContext(FacesContext facesContext) {
        if(facesContext == null) {
            facesContext = FacesContext.getCurrentInstance();

        }
        return facesContext.getApplication().evaluateExpressionGet(facesContext, "#{messageBean}", MessageBean.class); //$NON-NLS-1$
    }
	
    // Login Messages
	public String loginFailMessage = "";
	
	// Signup Messages
	public String signUpFailMessage = "";
	
	// LostPassword Messages
	public String resetPasswordMessage = "";
	
	// ChangePassword Messages
	public String changePasswordMessage = "";

	public String getLoginFailMessage() {
		return loginFailMessage;
	}

	public void setLoginFailMessage(String loginFailMessage) {
		this.loginFailMessage = loginFailMessage;
	}
	
	public String getSignUpFailMessage() {
		return signUpFailMessage;
	}

	public void setSignUpUsernameFailMessage(String signUpUsernameFailMessage) {
		this.signUpFailMessage = signUpUsernameFailMessage;
	}

	public String getResetPasswordMessage() {
		return resetPasswordMessage;
	}

	public void setResetPasswordMessage(String lostPasswordFailMessage) {
		this.resetPasswordMessage = lostPasswordFailMessage;
	}

	public String getChangePasswordMessage() {
		return changePasswordMessage;
	}

	public void setChangePasswordMessage(String changePasswordMessage) {
		this.changePasswordMessage = changePasswordMessage;
	}
}
