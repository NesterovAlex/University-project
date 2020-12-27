package com.nesterov.university.dao.exceptions;

public class NotUniqueEmailException extends Exception {

	private static final long serialVersionUID = 5535011598805225431L;

	public NotUniqueEmailException(String message) {
		super(message);
	}
}
