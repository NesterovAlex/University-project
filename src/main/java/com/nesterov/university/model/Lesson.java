package com.nesterov.university.model;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class Lesson {

	private long id;
	private Subject subject;
	private Audience audience;
	private Date date;
	private LessonTime time;
	private Teacher teacher;
	private List<Group> groups;

	public Lesson() {
	}

	public Lesson(Subject subject, Audience audience, Date date, LessonTime time, Teacher teacher) {
		this.subject = subject;
		this.audience = audience;
		this.date = date;
		this.time = time;
		this.teacher = teacher;
	}

	public Lesson(long id, Subject subject, Audience audience, Date date, LessonTime time, Teacher teacher) {
		this.id = id;
		this.subject = subject;
		this.audience = audience;
		this.date = date;
		this.time = time;
		this.teacher = teacher;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
