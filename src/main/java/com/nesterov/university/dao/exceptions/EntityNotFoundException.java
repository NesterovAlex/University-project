package com.nesterov.university.dao.exceptions;

import java.sql.SQLException;

public class EntityNotFoundException extends SQLException {

	private static final long serialVersionUID = 2894264840947049711L;

	private final String message;
	private final Exception exception;

	public EntityNotFoundException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}

	@Override
	public String toString() {
		return "EntityNotFoundException [message=" + message + ", exception=" + exception + "]";
	}
}
