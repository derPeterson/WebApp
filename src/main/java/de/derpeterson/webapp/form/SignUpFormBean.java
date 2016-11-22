package de.derpeterson.webapp.form;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "signUpFormBean")
@ViewScoped
public class SignUpFormBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final Integer minimumUsernameLength = 4;
	public static final Integer maximumUsernameLength = 16;
	
	public static final Integer minimumPasswordLength = 6;
	public static final Integer maximumPasswordLength = 20;
    
    public static SignUpFormBean getInstanceInFacesContext() {
        return getInstanceInFacesContext(null);
    }
    
    public static SignUpFormBean getInstanceInFacesContext(FacesContext facesContext) {
        if(facesContext == null) {
            facesContext = FacesContext.getCurrentInstance();

        }
        return facesContext.getApplication().evaluateExpressionGet(facesContext, "#{signUpFormBean}", SignUpFormBean.class); //$NON-NLS-1$
    }
    
    public SignUpFormBean() {
        super();
    }
    
    public void clear() {
        this.setUsername(null);
        this.setEmail(null);
        this.setEmailToConfirm(null);
        this.setPassword(null);
    }    
      
	private String username;

	private String email;

	private String emailToConfirm;

	private String password;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailToConfirm() {
		return emailToConfirm;
	}

	public void setEmailToConfirm(String emailToConfirm) {
		this.emailToConfirm = emailToConfirm;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
