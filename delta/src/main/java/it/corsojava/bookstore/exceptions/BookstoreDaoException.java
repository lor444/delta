package it.corsojava.bookstore.exceptions;

public class BookstoreDaoException extends BookstoreException {

	private static final long serialVersionUID = -527829301292527082L;

	public BookstoreDaoException() {
	}

	public BookstoreDaoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BookstoreDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public BookstoreDaoException(String message) {
		super(message);
	}

	public BookstoreDaoException(Throwable cause) {
		super(cause);
	}

}
