package com.mercadodecreditos.util;

public class ApplicationException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1193562440691284163L;

	public ApplicationException() {
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
