package com.nesterov.university.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Student;

@Component
public class StudentService {

	@Value(value = "${maxGroupSize}")
	private int maxGroupSize;
	private StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void create(Student student) throws NotCreateException, EntityNotFoundException, QueryNotExecuteException {
		if (isUniqueEmail(student) && isUniquePhone(student) && isUniqueAddress(student)
				&& findByGroupId(student.getGroupId()).size() <= maxGroupSize) {
			studentDao.create(student);
		}
	}

	public void delete(long id) throws NotExistException {
		studentDao.delete(id);
	}

	public Student get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		return studentDao.get(id);
	}

	public void update(Student student) throws NotCreateException, EntityNotFoundException, QueryNotExecuteException {
		if (isUniqueEmail(student) && isUniquePhone(student) && isUniqueAddress(student)
				&& findByGroupId(student.getGroupId()).size() <= maxGroupSize) {
			studentDao.update(student);
		}
	}

	public List<Student> getAll() throws EntityNotFoundException, QueryNotExecuteException {
		return studentDao.findAll();
	}

	public List<Student> findByGroupId(long id) throws QueryNotExecuteException, EntityNotFoundException {
		return studentDao.findByGroupId(id);
	}

	private boolean isUniqueEmail(Student student) throws EntityNotFoundException, QueryNotExecuteException {
		Student founded = studentDao.findByEmail(student.getEmail());
		return founded == null || founded.getId() == student.getId();
	}

	private boolean isUniquePhone(Student student) throws EntityNotFoundException, QueryNotExecuteException {
		Student founded = studentDao.findByPhone(student.getPhone());
		return founded == null || founded.getId() == student.getId();
	}

	private boolean isUniqueAddress(Student student) throws EntityNotFoundException, QueryNotExecuteException {
		Student founded = studentDao.findByAddress(student.getAddress());
		return founded == null || founded.getId() == student.getId();
	}
}
