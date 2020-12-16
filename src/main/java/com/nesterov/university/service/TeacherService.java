package com.nesterov.university.service;

import java.util.List;
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
		if (isUniqueEmail(teacher) && isUniquePhone(teacher) && isUniqueAddress(teacher)) {
			teacherDao.create(teacher);
		}
	}

	public void delete(long id) {
		teacherDao.delete(id);
	}

	public Teacher get(long id) {
		return teacherDao.get(id);
	}

	public void update(Teacher teacher) {
		if (isUniqueEmail(teacher) && isUniquePhone(teacher) && isUniqueAddress(teacher)) {
			teacherDao.update(teacher);
		}
	}

	public List<Teacher> getAll() {
		return teacherDao.findAll();
	}

	public List<Teacher> findBySubjectId(long id) {
		return teacherDao.findBySubjectId(id);
	}

	private boolean isUniqueEmail(Teacher teacher) {
		Teacher founded = teacherDao.findByEmail(teacher.getEmail());
		return founded == null || founded.getId() == teacher.getId();
	}

	private boolean isUniquePhone(Teacher teacher) {
		Teacher founded = teacherDao.findByPhone(teacher.getPhone());
		return founded == null || founded.getId() == teacher.getId();
	}

	private boolean isUniqueAddress(Teacher teacher) {
		Teacher founded = teacherDao.findByAddress(teacher.getAddress());
		return founded == null || founded.getId() == teacher.getId();
	}
}
