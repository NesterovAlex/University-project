package com.nesterov.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.model.Subject;

@Component
public class SubjectService {

	private static final Logger log = LoggerFactory.getLogger(SubjectService.class);

	private SubjectDao subjectDao;

	public SubjectService(SubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}

	public void create(Subject subject) {
		if (isUniqueName(subject)) {
			try {
				subjectDao.create(subject);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public void delete(long id) {
		try {
			subjectDao.delete(id);
		} catch (NotExistException e) {
			log.error(e.toString());
		}
	}

	public Subject get(long id) {
		Subject subject = null;
		try {
			return subjectDao.get(id);
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return subject;
	}

	public void update(Subject subject) {
		if (isUniqueName(subject)) {
			try {
				subjectDao.update(subject);
			} catch (EntityNotFoundException | NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public List<Subject> getAll() {
		List<Subject> subjects = null;
		try {
			return subjectDao.findAll();
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return subjects;
	}

	public List<Subject> findByTeacherId(long id) {
		List<Subject> subjects = null;
		try {
			return subjectDao.findByTeacherId(id);
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return subjects;
	}

	private boolean isUniqueName(Subject subject) {
		Subject founded = null;
		try {
			founded = subjectDao.findByName(subject.getName());
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return founded == null || founded.getId() == subject.getId();
	}
}
