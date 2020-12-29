package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.dao.exceptions.NotFoundException;
import com.nesterov.university.dao.exceptions.NotUniqueNameException;
import com.nesterov.university.model.Subject;

@Component
public class SubjectService {

	private SubjectDao subjectDao;

	public SubjectService(SubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}

	public void create(Subject subject) {
		if (isUniqueName(subject).getId() == subject.getId()) {
			subjectDao.create(subject);
		}
	}

	public void delete(long id) {
		if (!subjectDao.get(id).isPresent()) {
			throw new NotFoundException("Subject not found");
		}
		subjectDao.delete(id);
	}

	public Subject get(long id) {
		return subjectDao.get(id).orElseThrow(() -> new NotFoundException("Subject not found"));
	}

	public void update(Subject subject) {
		if (isUniqueName(subject).getId() == subject.getId()) {
			subjectDao.update(subject);
		}
	}

	public List<Subject> getAll() {
		List<Subject> subjects = subjectDao.findAll();
		if (subjects.isEmpty()) {
			throw new NotFoundException("Not found subjects");
		}
		return subjects;
	}

	public List<Subject> findByTeacherId(long id) {
		List<Subject> subjects = subjectDao.findByTeacherId(id);
		if (subjects.isEmpty()) {
			String message = format("Not found subjects by teacherId = '%s'", id);
			throw new NotFoundException(message);
		}
		return subjects;
	}

	private Subject isUniqueName(Subject subject) {
		Optional<Subject> founded = subjectDao.findByName(subject.getName());
		if (!founded.isPresent()) {
			String message = format("groupName '%s' is not unique", subject.getName());
			throw new NotUniqueNameException(message);
		}
		return founded.get();
	}
}
