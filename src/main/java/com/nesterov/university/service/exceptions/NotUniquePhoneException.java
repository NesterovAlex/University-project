package com.nesterov.university.service.exceptions;

public class NotUniquePhoneException extends RuntimeException {

	private static final long serialVersionUID = -7999776598367997493L;

	public NotUniquePhoneException(String message) {
		super(message);
	}
}
