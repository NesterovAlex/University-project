package com.nesterov.university.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class University {

	private String name;
	private List<Teacher> teachers;
	private List<Audience> audiences;
	private List<Subject> subjects;
	private List<Lesson> lessons;
	private List<Student> students;
	private List<Group> groups;
	
	public University() {
		this.teachers = new ArrayList<>();
		this.audiences = new ArrayList<>();
		this.subjects = new ArrayList<>();
		this.lessons = new ArrayList<>();
		this.students = new ArrayList<>();
		this.groups = new ArrayList<>();
	}
	
	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

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
