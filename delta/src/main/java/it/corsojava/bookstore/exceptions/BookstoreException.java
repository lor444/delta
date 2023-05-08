package it.corsojava.bookstore.exceptions;

public class BookstoreException extends Exception {

	private static final long serialVersionUID = -7853713366568462749L;

	public BookstoreException() {
		super();
	}

	public BookstoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BookstoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public BookstoreException(String message) {
		super(message);
	}

	public BookstoreException(Throwable cause) {
		super(cause);
	}

}
