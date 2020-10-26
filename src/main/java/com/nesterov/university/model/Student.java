package com.nesterov.university.model;

import java.time.LocalDate;

public class Student extends Person{
	
	public Student(String firstName, String lastName, LocalDate bithDate, String address, String email, String phone,
			Gender gender) {
		super(firstName, lastName, bithDate, address, email, phone, gender);
	}

	private String faculty;
	private String course;
	
	public String getFaculty() {
		return faculty;
	}
	
	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}
	
	public String getCourse() {
		return course;
	}
	
	public void setCourse(String course) {
		this.course = course;
	}
	
}
