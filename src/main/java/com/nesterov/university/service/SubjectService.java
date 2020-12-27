package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotUniqueNameException;
import com.nesterov.university.model.Subject;

@Component
public class SubjectService {

	private SubjectDao subjectDao;

	public SubjectService(SubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}

	public void create(Subject subject) throws NotCreateException, NotUniqueNameException {
		if (isUniqueName(subject)) {
			subjectDao.create(subject);
		}
	}

	public void delete(long id) throws NotDeleteException {
		subjectDao.delete(id);
	}

	public Subject get(long id) throws NotPresentEntityException {
		return subjectDao.get(id).orElseThrow(() -> new NotPresentEntityException("Subject not found"));
	}

	public void update(Subject subject) throws NotUniqueNameException {
		if (isUniqueName(subject)) {
			subjectDao.update(subject);
		}
	}

	public List<Subject> getAll() throws NotFoundEntitiesException {
		List<Subject> subjects = subjectDao.findAll();
		if (subjects.isEmpty()) {
			throw new NotFoundEntitiesException("Not found subjects");
		}
		return subjects;
	}

	public List<Subject> findByTeacherId(long id) throws NotFoundEntitiesException {
		List<Subject> subjects = subjectDao.findByTeacherId(id);
		if (subjects.isEmpty()) {
			String message = format("Not found subjects by teacherId = '%s'", id);
			throw new NotFoundEntitiesException(message);
		}
		return subjects;
	}

	private boolean isUniqueName(Subject subject) throws NotUniqueNameException {
		Optional<Subject> founded = subjectDao.findByName(subject.getName());
		if (founded.isPresent() && founded.orElse(new Subject()).getId() != subject.getId()) {
			String message = format("groupName '%s' is not unique", subject.getName());
			throw new NotUniqueNameException(message);
		}
		return !founded.isPresent() || founded.orElse(new Subject()).getId() == subject.getId();
	}
}
