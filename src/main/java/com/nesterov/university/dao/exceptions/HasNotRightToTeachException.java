package com.nesterov.university.dao.exceptions;

public class HasNotRightToTeachException extends RuntimeException {

	private static final long serialVersionUID = -8030692115099003757L;

	public HasNotRightToTeachException(String message) {
		super(message);
	}
}
