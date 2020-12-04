package com.nesterov.university.service;

import java.util.List;
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
		if (!hasEmail(student.getEmail()) && !hasPhone(student.getPhone()) && !hasAddress(student.getAddress())) {
			studentDao.create(student);
		}
	}

	public void delete(long id) {
		studentDao.delete(id);
	}

	public Student get(long id) {
		return studentDao.get(id);
	}

	public void update(Student student) {
		if (!hasEmail(student.getEmail()) && !hasPhone(student.getPhone()) && !hasAddress(student.getAddress())) {
			studentDao.update(student);
		}
	}

	public List<Student> getAll() {
		return studentDao.findAll();
	}

	public List<Student> findByGroupId(long id) {
		return studentDao.findByGroupId(id);
	}

	private boolean hasEmail(String email) {
		boolean hasName = false;
		if (studentDao.findByEmail(email) != null) {
			hasName = true;
		}
		return hasName;
	}

	private boolean hasPhone(String phone) {
		boolean hasPhone = false;
		if (studentDao.findByPhone(phone) != null) {
			hasPhone = true;
		}
		return hasPhone;
	}

	private boolean hasAddress(String address) {
		boolean hasAddress = false;
		if (studentDao.findByAddress(address) != null) {
			hasAddress = true;
		}
		return hasAddress;
	}
}
