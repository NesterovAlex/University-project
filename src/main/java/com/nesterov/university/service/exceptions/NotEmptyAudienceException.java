package com.nesterov.university.service.exceptions;

public class NotEmptyAudienceException extends RuntimeException {

	private static final long serialVersionUID = 1099598290752997153L;

	public NotEmptyAudienceException(String message) {
		super(message);
	}
}
