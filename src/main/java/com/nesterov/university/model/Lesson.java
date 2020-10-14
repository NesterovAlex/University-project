package com.nesterov.university.model;

import java.util.Date;
import java.util.List;

public class Lesson {
	
	private Subject subject;
	private Audience audience;
	private Date date;
	private LessonTime time;
	private Teacher teacher;
	private List<Group> groups;
	
	public Subject getSubject() {
		return subject;
	}
	
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public Audience getAudience() {
		return audience;
	}
	
	public void setAudience(Audience audience) {
		this.audience = audience;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public LessonTime getTime() {
		return time;
	}
	public void setTime(LessonTime time) {
		this.time = time;
	}
	
	public Teacher getTeacher() {
		return teacher;
	}
	
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public List<Group> getGroups() {
		return groups;
	}
	
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

}
