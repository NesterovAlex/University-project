package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Optional.ofNullable;
import static java.util.Optional.empty;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotUniqueNameException;
import com.nesterov.university.model.Subject;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SubjectServiceTest {

	@Mock
	private SubjectDao subjectDao;

	@InjectMocks
	private SubjectService subjectService;

	@Test
	void givenListOfExistsSubjects_whenGetAll_thenExpectedListOfSubjectsReturned() throws NotFoundEntitiesException {
		List<Subject> expected = new ArrayList<>();
		expected.add(new Subject(1, "Literature"));
		expected.add(new Subject(2, "Geography"));
		expected.add(new Subject(1, "Mathematic"));
		given(subjectDao.findAll()).willReturn(expected);

		List<Subject> actual = subjectService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenEmptyListSubjects_whenGetAll_thenNotFoundEntitiesExceptionThrown() throws NotFoundEntitiesException {
		given(subjectDao.findAll()).willReturn(new ArrayList<>());

		assertThrows(NotFoundEntitiesException.class, () -> subjectService.getAll());
	}

	@Test
	void givenExpectedSubject_whenGet_thenEqualSubjectReturned()
			throws EntityNotFoundException, NotPresentEntityException {
		Subject expected = new Subject(1, "Technology");
		given(subjectDao.get(expected.getId())).willReturn(ofNullable(expected));

		Subject actual = subjectService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenOptionalEmptySubject_whenGet_thenNotPresentEntityExceptionThrown()
			throws EntityNotFoundException, NotPresentEntityException {
		Subject expected = new Subject(5, "Machine Learning");
		given(subjectDao.get(expected.getId())).willReturn(empty());

		assertThrows(NotPresentEntityException.class, () -> subjectService.get(expected.getId()));
	}

	@Test
	void givenSubjectId_whenDelete_thenDeleted() throws NotDeleteException {
		int subjectId = 1;

		subjectService.delete(subjectId);

		verify(subjectDao).delete(subjectId);
	}

	@Test
	void givenSubject_whenUpdate_thenUpdated() throws NotUniqueNameException {
		Subject subject = new Subject(8, "Psychology");
		when(subjectDao.findByName(subject.getName())).thenReturn(ofNullable(subject));

		subjectService.update(subject);

		verify(subjectDao).update(subject);
	}

	@Test
	void givenExistingSubject_whenUpdate_thenUpdated() throws NotUniqueNameException {
		Subject subject = new Subject(7, "Nursing");
		when(subjectDao.findByName(subject.getName())).thenReturn(empty());

		subjectService.update(subject);

		verify(subjectDao).update(subject);
	}

	@Test
	void givenNonExistingSubject_whenUpdate_thenNotUpdatedNotUniqueNameExceptionThrown() throws NotUniqueNameException {
		Subject existingSubject = new Subject(7, "Nursing");
		Subject newSubject = new Subject(6, "Nursing");
		when(subjectDao.findByName(newSubject.getName())).thenReturn(ofNullable(existingSubject));

		assertThrows(NotUniqueNameException.class, () -> subjectService.update(newSubject));

		verify(subjectDao, never()).update(newSubject);
	}

	@Test
	void givenListOfExistsSubjects_whenFindByTeacherId_thenExpectedListOfSubjectsReturned()
			throws NotFoundEntitiesException {
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
	void givenEmptyListSubjects_whenFindByTeacherId_thenNotFoundEntitiesExceptionThrown()
			throws NotFoundEntitiesException {
		given(subjectDao.findByTeacherId(777)).willReturn(new ArrayList<>());

		assertThrows(NotFoundEntitiesException.class, () -> subjectService.findByTeacherId(777));
	}

	@Test
	void givenSubject_whenCreate_thenCreated() throws NotCreateException, NotUniqueNameException {
		Subject subject = new Subject(1, "Languages");
		when(subjectDao.findByName(subject.getName())).thenReturn(empty());

		subjectService.create(subject);

		verify(subjectDao).create(subject);
	}

	@Test
	void givenNonExistingSubject_whenCreate_thenCreated() throws NotCreateException, NotUniqueNameException {
		Subject existingSubject = new Subject(1, "Statistics");
		Subject newSubject = new Subject(1, "Statistics");
		when(subjectDao.findByName(newSubject.getName())).thenReturn(ofNullable(existingSubject));

		subjectService.create(newSubject);

		verify(subjectDao).create(newSubject);
	}

	@Test
	void givenDuplicateNames_whenCreate_thenNotCreatedAndNotUniqueNameExceptionThrown()
			throws NotCreateException, NotUniqueNameException {
		Subject expected = new Subject(2, "Statistics");
		Subject actual = new Subject(1, "Statistics");
		when(subjectDao.findByName(actual.getName())).thenReturn(ofNullable(actual));

		assertThrows(NotUniqueNameException.class, () -> subjectService.create(expected));

		verify(subjectDao, never()).create(expected);
	}
}
