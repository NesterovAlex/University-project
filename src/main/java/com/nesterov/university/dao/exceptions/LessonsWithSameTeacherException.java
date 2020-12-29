package com.nesterov.university.dao.exceptions;

public class LessonsWithSameTeacherException extends RuntimeException {

	private static final long serialVersionUID = 5066607876482901078L;

	public LessonsWithSameTeacherException(String message) {
		super(message);
	}
}
