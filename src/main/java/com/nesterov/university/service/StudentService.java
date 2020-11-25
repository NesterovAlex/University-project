package com.nesterov.university.service;

import org.springframework.stereotype.Component;

import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.model.Student;

@Component
public class StudentService {

	StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}
	
	public void createStudent(Student student) {
		studentDao.create(student);
	}
	
	public void deleteStudent(Student student) {
		studentDao.delete(student.getId());
	}
}
