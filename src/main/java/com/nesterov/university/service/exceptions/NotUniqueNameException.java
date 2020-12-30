package com.nesterov.university.service.exceptions;

public class NotUniqueNameException extends RuntimeException {

	private static final long serialVersionUID = 3010783112050829047L;

	public NotUniqueNameException(String message) {
		super(message);
	}
}
