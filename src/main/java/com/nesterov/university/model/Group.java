package com.nesterov.university.model;

import java.util.List;

public class Group {

	private long id;
	private String name;
	private List<Student> students;
	
	public Group() {}
	
	public Group(String name) {
		this.name = name;
	}
	
	public Group(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Student> getStudents() {
		return students;
	}
	
	public void setStudents(List<Student> students) {
		this.students = students;
	}
	
}
