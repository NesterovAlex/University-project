package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
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

	public void create(Student student) throws NotFoundEntitiesException, NotCreateException, NotUniqueEmailException,
			NotUniquePhoneException, NotUniqueAddressException {
		if (isUniqueEmail(student) && isUniquePhone(student) && isUniqueAddress(student)
				&& findByGroupId(student.getGroupId()).size() <= maxGroupSize) {
			studentDao.create(student);
		}
	}

	public void delete(long id) throws NotDeleteException {
		studentDao.delete(id);
	}

	public Student get(long id) throws NotPresentEntityException {
		String message = format("Audience with id = '%s' not found", id);
		return studentDao.get(id).orElseThrow(() -> new NotPresentEntityException(message));
	}

	public void update(Student student) throws NotFoundEntitiesException, NotUniqueEmailException,
			NotUniquePhoneException, NotUniqueAddressException {
		if (isUniqueEmail(student) && isUniquePhone(student) && isUniqueAddress(student)
				&& findByGroupId(student.getGroupId()).size() <= maxGroupSize) {
			studentDao.update(student);
		}
	}

	public List<Student> getAll() throws NotFoundEntitiesException {
		List<Student> students = studentDao.findAll();
		if (students.isEmpty()) {
			throw new NotFoundEntitiesException("Not found students");
		}
		return students;
	}

	public List<Student> findByGroupId(long id) throws NotFoundEntitiesException {
		List<Student> students = studentDao.findByGroupId(id);
		if (students.isEmpty()) {
			String message = format("Students with groupId = '%s' not found", id);
			throw new NotFoundEntitiesException(message);
		}
		return students;
	}

	private boolean isUniqueEmail(Student student) throws NotUniqueEmailException {
		Optional<Student> founded = studentDao.findByEmail(student.getEmail());
		if (founded.isPresent() && founded.orElse(new Student()).getId() != student.getId()) {
			String message = format("Email '%s' is not unique", student.getEmail());
			throw new NotUniqueEmailException(message);
		}
		return !founded.isPresent() || founded.orElse(new Student()).getId() == student.getId();
	}

	private boolean isUniquePhone(Student student) throws NotUniquePhoneException {
		Optional<Student> founded = studentDao.findByPhone(student.getPhone());
		if (founded.isPresent() && founded.orElse(new Student()).getId() != student.getId()) {
			String message = format("Phone '%s' is not unique", student.getPhone());
			throw new NotUniquePhoneException(message);
		}
		return !founded.isPresent() || founded.orElse(new Student()).getId() == student.getId();
	}

	private boolean isUniqueAddress(Student student) throws NotUniqueAddressException {
		Optional<Student> founded = studentDao.findByAddress(student.getAddress());
		if (founded.isPresent() && founded.orElse(new Student()).getId() != student.getId()) {
			String message = format("Address '%s' is not unique", student.getPhone());
			throw new NotUniqueAddressException(message);
		}
		return !founded.isPresent() || founded.orElse(new Student()).getId() == student.getId();
	}
}
