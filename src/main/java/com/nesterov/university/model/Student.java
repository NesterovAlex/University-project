package com.nesterov.university.model;

import java.sql.Date;

public class Student extends Person{
	
	private String faculty;
	private String course;
	
	public Student() {}
	
	public Student(String firstName, String lastName, Date bithDate, String address, String email, String phone,
			Gender gender) {
		super(firstName, lastName, bithDate, address, email, phone, gender);
	}
	
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
