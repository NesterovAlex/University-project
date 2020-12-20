package com.nesterov.university.dao.exceptions;

public class NotExistException extends Exception {

	private static final long serialVersionUID = -2100493166984796265L;

	private final String message;

	public NotExistException(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "NotExistException [message=" + message + "]";
	}

}
