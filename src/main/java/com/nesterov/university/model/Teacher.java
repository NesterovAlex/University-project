package com.nesterov.university.model;

import java.sql.Date;
import java.util.List;

public class Teacher extends Person{
	
	private List<Subject> subjects;
	
	public Teacher() {}
	
	public Teacher(String firstName, String lastName, Date date, String address, String email, String phone,
			Gender gender) {
		super(firstName, lastName, date, address, email, phone, gender);
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

}
