package de.derpeterson.webapp.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@SessionScoped
public class LocaleBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LocaleBean.class);

    private Locale locale;

    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language, boolean refreshPage) {
    	try {
	        locale = new Locale(language);
	        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
			Locale.setDefault(locale);
			LOGGER.debug("default locale set to '{}'", locale.toString());
			
			if(refreshPage) {
			    try {
			    	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
					ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
    	} catch(NullPointerException e) {
    		e.printStackTrace();
    	} 
    }

}