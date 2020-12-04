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
		if (!hasEmail(teacher.getEmail()) && !hasPhone(teacher.getPhone()) && !hasAddress(teacher.getAddress())) {
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
		if (!hasEmail(teacher.getEmail()) && !hasPhone(teacher.getPhone()) && !hasAddress(teacher.getAddress())) {
			teacherDao.update(teacher);
		}
	}

	public List<Teacher> getAll() {
		return teacherDao.findAll();
	}

	public List<Teacher> findBySubjectId(long id) {
		return teacherDao.findBySubjectId(id);
	}

	private boolean hasEmail(String email) {
		boolean hasName = false;
		if (teacherDao.findByEmail(email) != null) {
			hasName = true;
		}
		return hasName;
	}

	private boolean hasPhone(String phone) {
		boolean hasPhone = false;
		if (teacherDao.findByPhone(phone) != null) {
			hasPhone = true;
		}
		return hasPhone;
	}

	private boolean hasAddress(String address) {
		boolean hasAddress = false;
		if (teacherDao.findByAddress(address) != null) {
			hasAddress = true;
		}
		return hasAddress;
	}
}
