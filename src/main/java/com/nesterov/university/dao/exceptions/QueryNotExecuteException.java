package com.nesterov.university.dao.exceptions;

public class QueryNotExecuteException extends Exception {

	private static final long serialVersionUID = -7515758575103681261L;

	private final String message;
	private final Exception exception;
	
	public QueryNotExecuteException(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}

	@Override
	public String toString() {
		return "QueryNotExecuteException [message=" + message + ", exception=" + exception + "]";
	}
}
