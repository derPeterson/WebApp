package de.derpeterson.webapp.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class VerifyBean {
    
    public static VerifyBean getInstanceInFacesContext() {
        return getInstanceInFacesContext(null);
    }
    
    public static VerifyBean getInstanceInFacesContext(FacesContext facesContext) {
        if(facesContext == null) {
            facesContext = FacesContext.getCurrentInstance();

        }
        return facesContext.getApplication().evaluateExpressionGet(facesContext, "#{verifyBean}", VerifyBean.class); //$NON-NLS-1$
    }
	
    // Verification Key
	public String verificationKey = "";

	public String getVerificationKey() {
		return verificationKey;
	}

	public void setVerificationKey(String verificationKey) {
		this.verificationKey = verificationKey;
	}
}