package com.nesterov.university.service;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.model.Student;

@Component
public class StudentService {

	private StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void create(Student student) {
		if (!existsById(student.getId()))
			studentDao.create(student);
	}

	public void delete(long id) {
		studentDao.delete(id);
	}

	public Student get(long id) {
		return studentDao.get(id);
	}

	public void update(Student student) {
		studentDao.update(student);
	}

	public List<Student> getAll() {
		return studentDao.getAll();
	}

	public List<Student> findByGroupId(long id) {
		return studentDao.findByGroupId(id);
	}

	private boolean existsById(long id) {
		boolean exist;
		try {
			studentDao.get(id);
			exist = true;
		} catch (EmptyResultDataAccessException e) {
			exist = false;
		}
		return exist;
	}
}
