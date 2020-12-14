package com.nesterov.university.service;

import java.util.List;
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
		if (isUniqueName(subject)) {
			subjectDao.create(subject);
		}
	}

	public void delete(long id) {
		subjectDao.delete(id);
	}

	public Subject get(long id) {
		return subjectDao.get(id);
	}

	public void update(Subject subject) {
		if (isUniqueName(subject)) {
			subjectDao.update(subject);
		}
	}

	public List<Subject> getAll() {
		return subjectDao.findAll();
	}

	public List<Subject> findByTeacherId(long id) {
		return subjectDao.findByTeacherId(id);
	}

	private boolean isUniqueName(Subject subject) {
		Subject founded = subjectDao.findByName(subject.getName());  
		return founded == null || founded.getId() == subject.getId();
	}
}
