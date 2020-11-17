package com.nesterov.university.model;

import java.time.LocalDate;

public class Student extends Person {

	private long groupId;
	private String faculty;
	private String course;

	public Student() {
	}

	public Student(String firstName, String lastName, LocalDate bithDate, String address, String email, String phone,
			Gender gender) {
		super(firstName, lastName, bithDate, address, email, phone, gender);
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((faculty == null) ? 0 : faculty.hashCode());
		result = prime * result + (int) (groupId ^ (groupId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		super.equals(obj);
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (course == null) {
			if (other.course != null)
				return false;
		} 
		else if (!course.equals(other.course))
			return false;
		if (faculty == null) {
			if (other.faculty != null)
				return false;
		} 
		else if (!faculty.equals(other.faculty))
			return false;
		else if (groupId != other.groupId)
			return false;
		return true;
	}
}
