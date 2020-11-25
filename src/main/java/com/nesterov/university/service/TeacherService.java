package com.nesterov.university.service;

import org.springframework.stereotype.Component;

import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}
	
	public void createTeacher(Teacher teacher) {
		teacherDao.create(teacher);
	}
	
	public void deleteTeacher(Teacher teacher) {
		teacherDao.delete(teacher.getId());
	}
}
