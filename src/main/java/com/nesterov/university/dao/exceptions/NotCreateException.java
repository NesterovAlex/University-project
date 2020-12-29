package com.nesterov.university.dao.exceptions;

public class NotCreateException extends RuntimeException {

	private static final long serialVersionUID = 3303791011593530367L;

	public NotCreateException(String message) {
		super(message);
	}
}
