package com.nesterov.university.dao.exceptions;

public class HasLessonsWithSameGroupsException extends RuntimeException {

	private static final long serialVersionUID = -8428246250419013433L;

	public HasLessonsWithSameGroupsException(String message) {
		super(message);
	}
}
