package de.derpeterson.webapp.constants;

public class KnownPages {
	public final static String LOGIN_PAGE = "/index.xhtml?faces-redirect=true";
	public final static String LOGOUT_PAGE = "/logout.xhtml?faces-redirect=true";
    public final static String SIGNUP_PAGE = "/signup.xhtml?faces-redirect=true";
    public final static String HOME_PAGE = "/home/home.xhtml?faces-redirect=true";
    public final static String RESET_PAGE = "/resetPassword.xhtml?faces-redirect=true";
    
	public final static String LOGIN_PATTERN = "/login";
	public final static String LOGOUT_PATTERN = "/logout";
    public final static String SIGNUP_PATTERN = "/signup";
    public final static String HOME_PATTERN = "/home";
    public final static String RESET_PATTERN = "/resetPassword";
    
    public final static String PRETTY_LOGIN = "pretty:login";
	public final static String PRETTY_LOGOUT = "pretty:logout";
    public final static String PRETTY_SIGNUP = "pretty:signup";
    public final static String PRETTY_HOME = "pretty:home";
    public final static String PRETTY_VERIFY = "pretty:verify";
    public final static String PRETTY_RESET_PASSWORD = "pretty:resetPassword";
    public final static String PRETTY_CHANGE_PASSWORD = "pretty:changePassword";
    
    private final static String EMAIL_VERIFY_PATTERN = "verify/";
    private final static String EMAIL_CHANGE_PASSWORD_PATTERN = "changePassword/";
    
    public static String createEmailVerifyUrl(String verificationKey) {
    	return EMAIL_VERIFY_PATTERN + verificationKey;
    }

	public static String createEmailChangePasswordUrl(String passwordResetToken) {
		return EMAIL_CHANGE_PASSWORD_PATTERN + passwordResetToken;
	}
}
