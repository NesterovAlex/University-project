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
		if (!isUniqueEmail(teacher.getEmail()) && !isUniquePhone(teacher.getPhone())
				&& !isUniqueAddress(teacher.getAddress())) {
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
		if (isUniqueEmail(teacher.getEmail()) && isUniquePhone(teacher.getPhone())
				&& isUniqueAddress(teacher.getAddress())) {
			teacherDao.update(teacher);
		}
	}

	public List<Teacher> getAll() {
		return teacherDao.findAll();
	}

	public List<Teacher> findBySubjectId(long id) {
		return teacherDao.findBySubjectId(id);
	}

	private boolean isUniqueEmail(String email) {
		return teacherDao.findByEmail(email) != null;
	}

	private boolean isUniquePhone(String phone) {
		return teacherDao.findByPhone(phone) != null;
	}

	private boolean isUniqueAddress(String address) {
		return teacherDao.findByAddress(address) != null;
	}
}
