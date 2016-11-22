package de.derpeterson.webapp.form;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "resetPasswordFormBean")
@ViewScoped
public class ResetPasswordFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public static ResetPasswordFormBean getInstanceInFacesContext() {
        return getInstanceInFacesContext(null);
    }
    
    public static ResetPasswordFormBean getInstanceInFacesContext(FacesContext facesContext) {
        if(facesContext == null) {
            facesContext = FacesContext.getCurrentInstance();

        }
        return facesContext.getApplication().evaluateExpressionGet(facesContext, "#{resetPasswordFormBean}", ResetPasswordFormBean.class); //$NON-NLS-1$
    }
    
    public ResetPasswordFormBean() {
        super();
    }
    
    public void clear() {
        this.usernameOrEmail = null;
    }    
	
	private String usernameOrEmail;

	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}
}
