package com.nesterov.university.dao.exceptions;

public class NotUniquePhoneException extends Exception {

	private static final long serialVersionUID = -7999776598367997493L;

	public NotUniquePhoneException(String message) {
		super(message);
	}
}
