package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.model.Subject;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

	@Mock
	private SubjectDao subjectDao;

	@InjectMocks
	private SubjectService subjectService;

	@Test
	void givenListOfExistsSubjects_whenGetAll_thenExpectedListOfSubjectsReturned() throws EntityNotFoundException {
		List<Subject> expected = new ArrayList<>();
		expected.add(new Subject(1, "Literature"));
		expected.add(new Subject(2, "Geography"));
		expected.add(new Subject(1, "Mathematic"));
		given(subjectDao.findAll()).willReturn(expected);

		List<Subject> actual = subjectService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedSubject_whenGet_thenEqualSubjectReturned() throws EntityNotFoundException {
		Subject expected = new Subject(1, "Technology");
		given(subjectDao.get(expected.getId())).willReturn(expected);

		Subject actual = subjectService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenSubjectId_whenDelete_thenDeleted() throws NotExistException {
		int subjectId = 1;

		subjectService.delete(subjectId);

		verify(subjectDao).delete(subjectId);
	}

	@Test
	void givenSubject_whenUpdate_thenUpdated() throws EntityNotFoundException, NotCreateException {
		Subject subject = new Subject(8, "Psychology");
		when(subjectDao.findByName(subject.getName())).thenReturn(subject);

		subjectService.update(subject);

		verify(subjectDao).update(subject);
	}

	@Test
	void givenExistingSubject_whenUpdate_thenUpdated() throws EntityNotFoundException, NotCreateException {
		Subject subject = new Subject(7, "Nursing");
		when(subjectDao.findByName(subject.getName())).thenReturn(null);

		subjectService.update(subject);

		verify(subjectDao).update(subject);
	}

	@Test
	void givenNonExistingSubject_whenUpdate_thenNotUpdated() throws EntityNotFoundException, NotCreateException {
		Subject expected = new Subject(6, "Nursing");
		Subject actual = new Subject(7, "Nursing");
		when(subjectDao.findByName(actual.getName())).thenReturn(actual);

		subjectService.update(expected);

		verify(subjectDao, never()).update(expected);
	}

	@Test
	void givenListOfExistsSubjects_whenFindByTeacherId_thenExpectedListOfSubjectsReturned()
			throws EntityNotFoundException {
		int expected = 1;
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(1, "Literature"));
		subjects.add(new Subject(2, "Geography"));
		subjects.add(new Subject(1, "Mathematic"));
		given(subjectDao.findByTeacherId(expected)).willReturn(subjects);

		List<Subject> actual = subjectService.findByTeacherId(expected);

		assertEquals(subjects, actual);
	}

	@Test
	void givenSubject_whenCreate_thenCreated() throws EntityNotFoundException, NotCreateException {
		Subject subject = new Subject(1, "Languages");
		when(subjectDao.findByName(subject.getName())).thenReturn(null);

		subjectService.create(subject);

		verify(subjectDao).create(subject);
	}

	@Test
	void givenNonExistingSubject_whenCreate_thenCreated() throws EntityNotFoundException, NotCreateException {
		Subject existingSubject = new Subject(1, "Statistics");
		Subject newSubject = new Subject(1, "Statistics");
		when(subjectDao.findByName(newSubject.getName())).thenReturn(existingSubject);

		subjectService.create(newSubject);

		verify(subjectDao).create(newSubject);
	}

	@Test
	void givenDuplicateNames_whenCreate_thenNotCreated() throws EntityNotFoundException, NotCreateException {
		Subject expected = new Subject(2, "Statistics");
		Subject actual = new Subject(1, "Statistics");
		when(subjectDao.findByName(actual.getName())).thenReturn(actual);

		subjectService.create(expected);

		verify(subjectDao, never()).create(expected);
	}
}
