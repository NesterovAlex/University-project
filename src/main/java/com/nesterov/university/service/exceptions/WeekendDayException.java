package com.nesterov.university.service.exceptions;

public class WeekendDayException extends RuntimeException {

	private static final long serialVersionUID = 902257046552622991L;

	public WeekendDayException(String message) {
		super(message);
	}
}
