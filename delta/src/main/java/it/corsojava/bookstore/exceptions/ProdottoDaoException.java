package it.corsojava.bookstore.exceptions;

public class ProdottoDaoException extends BookstoreDaoException{

	private static final long serialVersionUID = 4498789075809959701L;

	public ProdottoDaoException() {
		super();
	}

	public ProdottoDaoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProdottoDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProdottoDaoException(String message) {
		super(message);
	}

	public ProdottoDaoException(Throwable cause) {
		super(cause);
	}
	
}
