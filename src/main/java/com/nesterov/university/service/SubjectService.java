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
		if (isUniqueName(subject.getName())) {
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
		if (!isUniqueName(subject.getName())
				&& subjectDao.findByName(subject.getName()).getName().equals(subject.getName())) {
			subjectDao.update(subject);
		}
	}

	public List<Subject> getAll() {
		return subjectDao.findAll();
	}

	public List<Subject> findByTeacherId(long id) {
		return subjectDao.findByTeacherId(id);
	}

	private boolean isUniqueName(String name) {
		return subjectDao.findByName(name).getName() == null;
	}
}
