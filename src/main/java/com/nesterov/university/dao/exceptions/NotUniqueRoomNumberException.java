package com.nesterov.university.dao.exceptions;

public class NotUniqueRoomNumberException extends Exception {

	private static final long serialVersionUID = 2003874395645982892L;

	public NotUniqueRoomNumberException(String message) {
		super(message);
	}
}
