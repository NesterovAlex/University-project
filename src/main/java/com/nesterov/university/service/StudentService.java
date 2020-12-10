package com.nesterov.university.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.model.Student;

@Component
public class StudentService {

	@Value(value = "${groupCapacity}")
	private int groupCapacity;
	private StudentDao studentDao;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void create(Student student) {
		if (isUniqueEmail(student.getEmail()) && isUniquePhone(student.getPhone())
				&& isUniqueAddress(student.getAddress())
				&& findByGroupId(student.getGroupId()).size() <= groupCapacity) {
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
		if (isUniqueEmail(student.getEmail()) && isUniquePhone(student.getPhone())
				&& isUniqueAddress(student.getAddress()) && findByGroupId(student.getGroupId()).size() <= 30) {
			studentDao.update(student);
		}
	}

	public List<Student> getAll() {
		return studentDao.findAll();
	}

	public List<Student> findByGroupId(long id) {
		return studentDao.findByGroupId(id);
	}

	private boolean isUniqueEmail(String email) {
		return studentDao.findByEmail(email) == null;
	}

	private boolean isUniquePhone(String phone) {
		return studentDao.findByPhone(phone) == null;
	}

	private boolean isUniqueAddress(String address) {
		return studentDao.findByAddress(address) == null;
	}
}
