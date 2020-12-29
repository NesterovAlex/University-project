package com.nesterov.university.dao.exceptions;

public class NotRightTimeException extends RuntimeException {

	private static final long serialVersionUID = -5643802126412490377L;

	public NotRightTimeException(String message) {
		super(message);
	}

}
