package com.nesterov.university.dao.exceptions;

public class NotFoundEntitiesException extends RuntimeException {

	private static final long serialVersionUID = -4089185033197061444L;

	public NotFoundEntitiesException(String message) {
		super(message);
	}
}
