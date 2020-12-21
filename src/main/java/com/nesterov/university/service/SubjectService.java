package com.nesterov.university.service;

import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Subject;

@Component
public class SubjectService {

	private SubjectDao subjectDao;

	public SubjectService(SubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}

	public void create(Subject subject) throws NotCreateException, EntityNotFoundException, QueryNotExecuteException {
		if (isUniqueName(subject)) {
			subjectDao.create(subject);
		}
	}

	public void delete(long id) throws NotExistException {
		subjectDao.delete(id);
	}

	public Subject get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		return subjectDao.get(id);
	}

	public void update(Subject subject) throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		if (isUniqueName(subject)) {
			subjectDao.update(subject);
		}
	}

	public List<Subject> getAll() throws EntityNotFoundException, QueryNotExecuteException {
		return subjectDao.findAll();
	}

	public List<Subject> findByTeacherId(long id) throws QueryNotExecuteException, EntityNotFoundException {
		return subjectDao.findByTeacherId(id);
	}

	private boolean isUniqueName(Subject subject) throws EntityNotFoundException, QueryNotExecuteException {
		Subject founded = subjectDao.findByName(subject.getName());
		return founded == null || founded.getId() == subject.getId();
	}
}
