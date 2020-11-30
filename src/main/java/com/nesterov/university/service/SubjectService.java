package com.nesterov.university.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.model.Subject;

@Component
public class SubjectService {

	private SubjectDao subjectDao;

	public SubjectService(SubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}

	public void create(Subject subject) {
		if (!existsById(subject.getId()))
			subjectDao.create(subject);
	}

	public void delete(long id) {
		subjectDao.delete(id);
	}

	public Subject get(long id) {
		return subjectDao.get(id);
	}

	public void update(Subject subject) {
		subjectDao.update(subject);
	}

	public List<Subject> getAll() {
		return subjectDao.getAll();
	}

	public List<Subject> findByTeacherId(long id) {
		return subjectDao.findByTeacherId(id);
	}

	private boolean existsById(long id) {
		boolean exist;
		try {
			subjectDao.get(id);
			exist = true;
		} catch (EmptyResultDataAccessException e) {
			exist = false;
		}
		return exist;
	}
}
