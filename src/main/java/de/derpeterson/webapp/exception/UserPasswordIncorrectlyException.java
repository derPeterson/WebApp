package de.derpeterson.webapp.exception;

public class UserPasswordIncorrectlyException extends Exception {
	
	private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public UserPasswordIncorrectlyException() {
        super();
    }

    /**
     * @param message
     */
    public UserPasswordIncorrectlyException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public UserPasswordIncorrectlyException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public UserPasswordIncorrectlyException(String message, Throwable cause) {
        super(message, cause);
    }
}
