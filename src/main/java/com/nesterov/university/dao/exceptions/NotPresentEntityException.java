package com.nesterov.university.dao.exceptions;

public class NotPresentEntityException extends Exception {

	private static final long serialVersionUID = 3497723982125213274L;

	public NotPresentEntityException(String message) {
		super(message);
	}
}
