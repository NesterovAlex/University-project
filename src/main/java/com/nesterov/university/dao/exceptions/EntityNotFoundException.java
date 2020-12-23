package com.nesterov.university.dao.exceptions;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2894264840947049711L;

	public EntityNotFoundException(String message, Exception exception) {
		super(message, exception);
	}
}
