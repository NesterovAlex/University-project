package com.nesterov.university.service.exceptions;

public class HasNotEnoughtPlacesException extends RuntimeException {

	private static final long serialVersionUID = -7304613374099776611L;

	public HasNotEnoughtPlacesException(String message) {
		super(message);
	}
}
