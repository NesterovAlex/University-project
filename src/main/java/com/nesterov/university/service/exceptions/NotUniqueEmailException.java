package com.nesterov.university.service.exceptions;

public class NotUniqueEmailException extends RuntimeException {

	private static final long serialVersionUID = 5535011598805225431L;

	public NotUniqueEmailException(String message) {
		super(message);
	}
}
