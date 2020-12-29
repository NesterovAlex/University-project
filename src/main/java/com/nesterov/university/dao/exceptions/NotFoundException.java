package com.nesterov.university.dao.exceptions;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4094461231950143868L;

	public NotFoundException(String message) {
		super(message);
	}
}
