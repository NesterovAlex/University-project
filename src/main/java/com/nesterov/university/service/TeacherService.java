package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.service.exceptions.NotFoundException;
import com.nesterov.university.service.exceptions.NotUniqueAddressException;
import com.nesterov.university.service.exceptions.NotUniqueEmailException;
import com.nesterov.university.service.exceptions.NotUniquePhoneException;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	public void create(Teacher teacher) {
		verifyEmailIsUnique(teacher);
		verifyPhoneIsUnique(teacher);
		verifyAddressIsUnique(teacher);
		teacherDao.create(teacher);
	}

	public void delete(long id) {
		if (!teacherDao.get(id).isPresent()) {
			String message = format("Teacher with id = '%s' not found", id);
			throw new NotFoundException(message);
		}
		teacherDao.delete(id);
	}

	public Teacher get(long id) {
		Optional<Teacher> teacherOptional = teacherDao.get(id);
		if (!teacherOptional.isPresent()) {
			String message = format("Teacher with id = '%s' not found", id);
			throw new NotFoundException(message);
		}
		return teacherOptional.orElse(new Teacher());
	}

	public void update(Teacher teacher) {
		verifyEmailIsUnique(teacher);
		verifyPhoneIsUnique(teacher);
		verifyAddressIsUnique(teacher);
		teacherDao.update(teacher);
	}

	public List<Teacher> getAll() {
		return teacherDao.findAll();
	}

	public List<Teacher> findBySubjectId(long id) {
		List<Teacher> teachers = teacherDao.findBySubjectId(id);
		if (teachers.isEmpty()) {
			String message = format("Teachers with subjectId = '%s' not found", id);
			throw new NotFoundException(message);
		}
		return teachers;
	}

	private void verifyEmailIsUnique(Teacher teacher) {
		if (!teacherDao.findByEmail(teacher.getEmail()).isPresent()) {
			String message = format("Teacher email = '%s' not unique", teacher.getEmail());
			throw new NotUniqueEmailException(message);
		}
	}

	private void verifyPhoneIsUnique(Teacher teacher) {
		if (!teacherDao.findByPhone(teacher.getPhone()).isPresent()) {
			String message = format("Teacher phone = '%s' not unique", teacher.getPhone());
			throw new NotUniquePhoneException(message);
		}
	}

	private void verifyAddressIsUnique(Teacher teacher) {
		if (!teacherDao.findByAddress(teacher.getAddress()).isPresent()) {
			String message = format("Teacher address = '%s' not unoque", teacher.getAddress());
			throw new NotUniqueAddressException(message);
		}
	}
}
