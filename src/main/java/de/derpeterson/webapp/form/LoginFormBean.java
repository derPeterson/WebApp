package de.derpeterson.webapp.form;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "loginFormBean")
@ViewScoped
public class LoginFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public static LoginFormBean getInstanceInFacesContext() {
        return getInstanceInFacesContext(null);
    }
    
    public static LoginFormBean getInstanceInFacesContext(FacesContext facesContext) {
        if(facesContext == null) {
            facesContext = FacesContext.getCurrentInstance();

        }
        return facesContext.getApplication().evaluateExpressionGet(facesContext, "#{loginFormBean}", LoginFormBean.class); //$NON-NLS-1$
    }
    
    public LoginFormBean() {
        super();
    }
    
    public void clear() {
        this.usernameOrEmail = null;
        this.userPassword = null;
    }    
	
	private String usernameOrEmail;

	private String userPassword;

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}
}
