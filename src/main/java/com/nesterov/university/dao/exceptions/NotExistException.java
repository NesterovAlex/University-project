package com.nesterov.university.dao.exceptions;

public class NotExistException extends Exception {

	private static final long serialVersionUID = -2100493166984796265L;

	private final String message;
	private final Exception exception;

	public NotExistException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}

	@Override
	public String toString() {
		return "NotExistException [message=" + message + ", exception=" + exception + "]";
	}
}
