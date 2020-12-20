package com.nesterov.university.dao.exceptions;

public class EntityNotFoundException extends Exception{

	private static final long serialVersionUID = 2894264840947049711L;

	private final String message;
	
	public EntityNotFoundException(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "EntityNotFoundException [message=" + message + "]";
	}
}
