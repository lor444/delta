package it.corsojava.bookstore.exceptions;

public class UtenteDaoValidationException extends UtenteDaoException {

	private static final long serialVersionUID = 7028788892576932611L;

	public UtenteDaoValidationException() {
	}

	public UtenteDaoValidationException(String message, Throwable cause, boolean enableSuppression,boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace); 
	}

	public UtenteDaoValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtenteDaoValidationException(String message) {
		super(message);
	}

	public UtenteDaoValidationException(Throwable cause) {
		super(cause);
	}

}
