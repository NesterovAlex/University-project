package com.nesterov.university.model;

import java.util.List;

public class Teacher extends Person{
	
	private List<Subject> subjects;

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

}
