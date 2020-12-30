package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.service.exceptions.NotFoundException;
import com.nesterov.university.service.exceptions.NotUniqueAddressException;
import com.nesterov.university.service.exceptions.NotUniqueEmailException;
import com.nesterov.university.service.exceptions.NotUniquePhoneException;
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
		checkUniqueEmail(student);
		checkUniquePhone(student);
		checkUniqueAddress(student);
		if (findByGroupId(student.getGroupId()).size() <= maxGroupSize) {
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
		return studentDao.get(id).orElseThrow(() -> new NotFoundException(message));
	}

	public void update(Student student) {
		checkUniqueEmail(student);
		checkUniquePhone(student);
		checkUniqueAddress(student);
		if (findByGroupId(student.getGroupId()).size() <= maxGroupSize) {
			studentDao.update(student);
		}
	}

	public List<Student> getAll() {
		List<Student> students = studentDao.findAll();
		if (students.isEmpty()) {
			throw new NotFoundException("Not found students");
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

	private void checkUniqueEmail(Student student) {
		if (!studentDao.findByEmail(student.getEmail()).isPresent()) {
			String message = format("Email '%s' is not unique", student.getEmail());
			throw new NotUniqueEmailException(message);
		}
	}

	private void checkUniquePhone(Student student) {
		if (!studentDao.findByPhone(student.getPhone()).isPresent()) {
			String message = format("Phone '%s' is not unique", student.getPhone());
			throw new NotUniquePhoneException(message);
		}
	}

	private void checkUniqueAddress(Student student) {
		if (!studentDao.findByAddress(student.getAddress()).isPresent()) {
			String message = format("Address '%s' is not unique", student.getAddress());
			throw new NotUniqueAddressException(message);
		}
	}
}
