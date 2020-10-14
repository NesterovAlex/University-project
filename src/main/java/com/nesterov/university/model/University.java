package com.nesterov.university.model;

import java.util.List;

public class University {

	private String name;
	private List<Teacher> teachers;
	private List<Audience> audiences;
	private List<Subject> subjects;
	private List<Lesson> lessons;
	
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
	
	public List<Audience> getAudiences() {
		return audiences;
	}
	
	public void setAudiences(List<Audience> audiences) {
		this.audiences = audiences;
	}
	
	public List<Subject> getSubjects() {
		return subjects;
	}
	
	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
	
	public List<Lesson> getLessons() {
		return lessons;
	}
	
	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}
	
}
