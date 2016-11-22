package de.derpeterson.webapp.form;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "changePasswordFormBean")
@ViewScoped
public class ChangePasswordFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public static ChangePasswordFormBean getInstanceInFacesContext() {
        return getInstanceInFacesContext(null);
    }
    
    public static ChangePasswordFormBean getInstanceInFacesContext(FacesContext facesContext) {
        if(facesContext == null) {
            facesContext = FacesContext.getCurrentInstance();

        }
        return facesContext.getApplication().evaluateExpressionGet(facesContext, "#{changePasswordFormBean}", ChangePasswordFormBean.class);
    }
    
    public ChangePasswordFormBean() {
        super();
    }
    
    public void clear() {
        this.setPasswordResetToken(null);
    }    
	
	private String passwordResetToken;
	
	private String newPassword;
	
	private String newPasswordToConfirm;
	
	public String getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(String passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordToConfirm() {
		return newPasswordToConfirm;
	}

	public void setNewPasswordToConfirm(String newPasswordToConfirm) {
		this.newPasswordToConfirm = newPasswordToConfirm;
	}
}
