package com.nesterov.university.model;

public enum Gender {
	MALE("MALE"),
	FEMALE("FEMALE");

	private String title;

	Gender(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
