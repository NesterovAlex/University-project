package com.nesterov.university.model;

import java.util.List;

public class Group {

	private String nameString;
	private List<Student> students;
	
	public String getNameString() {
		return nameString;
	}
	
	public void setNameString(String nameString) {
		this.nameString = nameString;
	}
	
	public List<Student> getStudents() {
		return students;
	}
	
	public void setStudents(List<Student> students) {
		this.students = students;
	}
	
}
