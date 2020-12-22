package com.nesterov.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.model.Student;

@Component
public class StudentService {

	private static final Logger log = LoggerFactory.getLogger(StudentService.class);

	@Value(value = "${maxGroupSize}")
	private int maxGroupSize;
	private StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void create(Student student) {
		if (isUniqueEmail(student) && isUniquePhone(student) && isUniqueAddress(student)
				&& findByGroupId(student.getGroupId()).size() <= maxGroupSize) {
			try {
				studentDao.create(student);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public void delete(long id) {
		try {
			studentDao.delete(id);
		} catch (NotExistException e) {
			log.error(e.toString());
		}
	}

	public Student get(long id) throws EntityNotFoundException {
		return studentDao.get(id);
	}

	public void update(Student student) {
		if (isUniqueEmail(student) && isUniquePhone(student) && isUniqueAddress(student)
				&& findByGroupId(student.getGroupId()).size() <= maxGroupSize) {
			try {
				studentDao.update(student);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public List<Student> getAll() {
		List<Student> students = null;
		try {
			return studentDao.findAll();
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return students;
	}

	public List<Student> findByGroupId(long id) {
		List<Student> students = null;
		try {
			return studentDao.findByGroupId(id);
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return students;
	}

	private boolean isUniqueEmail(Student student) {
		Student founded = null;
		try {
			founded = studentDao.findByEmail(student.getEmail());
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return founded == null || founded.getId() == student.getId();
	}

	private boolean isUniquePhone(Student student) {
		Student founded = null;
		try {
			founded = studentDao.findByPhone(student.getPhone());
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return founded == null || founded.getId() == student.getId();
	}

	private boolean isUniqueAddress(Student student) {
		Student founded = null;
		try {
			founded = studentDao.findByAddress(student.getAddress());
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return founded == null || founded.getId() == student.getId();
	}
}
