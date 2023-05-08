package it.corsojava.bookstore.exceptions;

public class UtenteDaoException extends BookstoreDaoException{

	private static final long serialVersionUID = 4498789075809959701L;

	public UtenteDaoException() {
		super();
	}

	public UtenteDaoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UtenteDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtenteDaoException(String message) {
		super(message);
	}

	public UtenteDaoException(Throwable cause) {
		super(cause);
	}
	
}
