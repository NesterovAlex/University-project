package com.nesterov.university.service;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	public void create(Teacher teacher) {
		if (!existsById(teacher.getId()))
			teacherDao.create(teacher);
	}

	public void delete(long id) {
		teacherDao.delete(id);
	}

	public Teacher get(long id) {
		return teacherDao.get(id);
	}

	public void update(Teacher teacher) {
		teacherDao.update(teacher);
	}

	public List<Teacher> getAll() {
		return teacherDao.getAll();
	}

	public List<Teacher> findBySubjectId(long id) {
		return teacherDao.findBySubjectId(id);
	}

	private boolean existsById(long id) {
		boolean exist;
		try {
			teacherDao.get(id);
			exist = true;
		} catch (EmptyResultDataAccessException e) {
			exist = false;
		}
		return exist;
	}
}
