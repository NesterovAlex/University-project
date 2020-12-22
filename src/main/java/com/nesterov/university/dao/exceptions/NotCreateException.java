package com.nesterov.university.dao.exceptions;

public class NotCreateException extends Exception {

	private static final long serialVersionUID = 3303791011593530367L;

	private final String message;
	private final Exception exception;

	public NotCreateException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}

	@Override
	public String toString() {
		return "NotCreateException [message=" + message + ", exception=" + exception + "]";
	}
}
