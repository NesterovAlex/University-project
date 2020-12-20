package com.nesterov.university.dao.exceptions;

public class NotCreateException extends Exception {

	private static final long serialVersionUID = 3303791011593530367L;
	
	private final String message;

	public NotCreateException(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "NotCreatedException [message=" + message + "]";
	}
}
