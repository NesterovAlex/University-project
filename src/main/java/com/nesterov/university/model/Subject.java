package com.nesterov.university.model;

import java.util.List;

public class Subject {

	private String name;
	private List<Teacher> teachers;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Teacher> getTeachers() {
		return teachers;
	}
	
	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}
	
}
