package com.nesterov.university.dao.exceptions;

public class LessonsWithSameTeacherException extends Exception {

	private static final long serialVersionUID = 5066607876482901078L;

	public LessonsWithSameTeacherException(String message) {
		super(message);
	}
}
