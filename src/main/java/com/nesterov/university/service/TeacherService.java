package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.dao.exceptions.NotFoundException;
import com.nesterov.university.dao.exceptions.NotUniqueAddressException;
import com.nesterov.university.dao.exceptions.NotUniqueEmailException;
import com.nesterov.university.dao.exceptions.NotUniquePhoneException;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	public void create(Teacher teacher) {
		long id = teacher.getId();
		if (isUniqueEmail(teacher).getId() == id && isUniquePhone(teacher).getId() == id
				&& isUniqueAddress(teacher).getId() == id) {
			teacherDao.create(teacher);
		}
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
		long id = teacher.getId();
		if (isUniqueEmail(teacher).getId() == id && isUniquePhone(teacher).getId() == id
				&& isUniqueAddress(teacher).getId() == id) {
			teacherDao.update(teacher);
		}
	}

	public List<Teacher> getAll() {
		List<Teacher> teachers = teacherDao.findAll();
		if (teachers.isEmpty()) {
			throw new NotFoundException("Teachers not found");
		}
		return teachers;
	}

	public List<Teacher> findBySubjectId(long id) {
		List<Teacher> teachers = teacherDao.findBySubjectId(id);
		if (teachers.isEmpty()) {
			String message = format("Teachers with subjectId = '%s' not found", id);
			throw new NotFoundException(message);
		}
		return teachers;
	}

	private Teacher isUniqueEmail(Teacher teacher) {
		Optional<Teacher> founded = teacherDao.findByEmail(teacher.getEmail());
		if (!founded.isPresent()) {
			String message = format("Teacher email = '%s' not unique", teacher.getEmail());
			throw new NotUniqueEmailException(message);
		}
		return founded.get();
	}

	private Teacher isUniquePhone(Teacher teacher) {
		Optional<Teacher> founded = teacherDao.findByPhone(teacher.getPhone());
		if (!founded.isPresent()) {
			String message = format("Teacher phone = '%s' not unique", teacher.getPhone());
			throw new NotUniquePhoneException(message);
		}
		return founded.get();
	}

	private Teacher isUniqueAddress(Teacher teacher) {
		Optional<Teacher> founded = teacherDao.findByAddress(teacher.getAddress());
		if (!founded.isPresent()) {
			String message = format("Teacher address = '%s' not unoque", teacher.getAddress());
			throw new NotUniqueAddressException(message);
		}
		return founded.get();
	}
}
