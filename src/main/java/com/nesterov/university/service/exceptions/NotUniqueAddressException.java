package com.nesterov.university.service.exceptions;

public class NotUniqueAddressException extends RuntimeException {

	private static final long serialVersionUID = -5118175056763288175L;

	public NotUniqueAddressException(String message) {
		super(message);
	}
}
