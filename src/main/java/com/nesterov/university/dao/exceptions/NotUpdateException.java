package com.nesterov.university.dao.exceptions;

public class NotUpdateException extends RuntimeException {

	private static final long serialVersionUID = 8903373034987319064L;

	public NotUpdateException(String message, Exception e) {
		super(message, e);
	}
}
