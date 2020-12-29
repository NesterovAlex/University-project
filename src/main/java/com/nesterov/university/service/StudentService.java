package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotFoundException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotUniqueAddressException;
import com.nesterov.university.dao.exceptions.NotUniqueEmailException;
import com.nesterov.university.dao.exceptions.NotUniquePhoneException;
import com.nesterov.university.model.Student;

@Component
public class StudentService {

	@Value(value = "${maxGroupSize}")
	private int maxGroupSize;
	private StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void create(Student student) {
		long id = student.getId();
		if (isUniqueEmail(student).getId() == id && isUniquePhone(student).getId() == id
				&& isUniqueAddress(student).getId() == id
				&& findByGroupId(student.getGroupId()).size() <= maxGroupSize) {
			studentDao.create(student);
		}
	}

	public void delete(long id) {
		if (!studentDao.get(id).isPresent()) {
			String message = format("Audience with id = '%s' not found", id);
			throw new NotFoundException(message);
		}
		studentDao.delete(id);
	}

	public Student get(long id) {
		String message = format("Audience with id = '%s' not found", id);
		return studentDao.get(id).orElseThrow(() -> new NotPresentEntityException(message));
	}

	public void update(Student student) {
		long id = student.getId();
		if (isUniqueEmail(student).getId() == id && isUniquePhone(student).getId() == id
				&& isUniqueAddress(student).getId() == id
				&& findByGroupId(student.getGroupId()).size() <= maxGroupSize) {
			studentDao.update(student);
		}
	}

	public List<Student> getAll() {
		List<Student> students = studentDao.findAll();
		if (students.isEmpty()) {
			throw new NotFoundEntitiesException("Not found students");
		}
		return students;
	}

	public List<Student> findByGroupId(long id) {
		List<Student> students = studentDao.findByGroupId(id);
		if (students.isEmpty()) {
			String message = format("Students with groupId = '%s' not found", id);
			throw new NotFoundException(message);
		}
		return students;
	}

	private Student isUniqueEmail(Student student) {
		Optional<Student> founded = studentDao.findByEmail(student.getEmail());
		if (!founded.isPresent()) {
			String message = format("Email '%s' is not unique", student.getEmail());
			throw new NotUniqueEmailException(message);
		}
		return founded.get();
	}

	private Student isUniquePhone(Student student) {
		Optional<Student> founded = studentDao.findByPhone(student.getPhone());
		if (!founded.isPresent()) {
			String message = format("Phone '%s' is not unique", student.getPhone());
			throw new NotUniquePhoneException(message);
		}
		return founded.get();
	}

	private Student isUniqueAddress(Student student) {
		Optional<Student> founded = studentDao.findByAddress(student.getAddress());
		if (!founded.isPresent()) {
			String message = format("Address '%s' is not unique", student.getAddress());
			throw new NotUniqueAddressException(message);
		}
		return founded.get();
	}
}
