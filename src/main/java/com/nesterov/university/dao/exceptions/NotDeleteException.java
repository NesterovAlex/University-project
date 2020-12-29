package com.nesterov.university.dao.exceptions;

public class NotDeleteException extends RuntimeException {

	private static final long serialVersionUID = 6532291386624897986L;

	public NotDeleteException(String message) {
		super(message);
	}
}
