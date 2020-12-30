package com.nesterov.university.service.exceptions;

public class NotUniqueRoomNumberException extends RuntimeException {

	private static final long serialVersionUID = 2003874395645982892L;

	public NotUniqueRoomNumberException(String message) {
		super(message);
	}
}
