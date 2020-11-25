package com.nesterov.university.model;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Lesson {

	private long id;
	private Subject subject;
	private Audience audience;
	private LocalDate date;
	private LessonTime time;
	private Teacher teacher;
	private List<Group> groups;

	public Lesson() {
	}

	public Lesson(Subject subject, Audience audience, LocalDate date, LessonTime time, Teacher teacher) {
		this.subject = subject;
		this.audience = audience;
		this.date = date;
		this.time = time;
		this.teacher = teacher;
	}

	public Lesson(long id, Subject subject, Audience audience, LocalDate date, LessonTime time, Teacher teacher) {
		this.id = id;
		this.subject = subject;
		this.audience = audience;
		this.date = date;
		this.time = time;
		this.teacher = teacher;
	}

	public Lesson(Subject subject, Audience audience, LocalDate date, LessonTime time, Teacher teacher, List<Group> groups) {
		this.subject = subject;
		this.audience = audience;
		this.date = date;
		this.time = time;
		this.teacher = teacher;
		this.groups = groups;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((audience == null) ? 0 : audience.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lesson other = (Lesson) obj;
		if (audience == null) {
			if (other.audience != null)
				return false;
		} 
		else if (!audience.equals(other.audience))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} 
		else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} 
		else if (!subject.equals(other.subject))
			return false;
		if (teacher == null) {
			if (other.teacher != null)
				return false;
		} 
		else if (!teacher.equals(other.teacher))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} 
		else if (!time.equals(other.time))
			return false;
		return true;
	}
}
