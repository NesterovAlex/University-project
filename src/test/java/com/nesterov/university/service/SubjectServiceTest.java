package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Optional.of;
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
import com.nesterov.university.dao.exceptions.NotFoundException;
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
	void givenListOfExistsSubjects_whenGetAll_thenExpectedListOfSubjectsReturned() {
		List<Subject> expected = new ArrayList<>();
		expected.add(new Subject(1, "Literature"));
		expected.add(new Subject(2, "Geography"));
		expected.add(new Subject(1, "Mathematic"));
		given(subjectDao.findAll()).willReturn(expected);

		List<Subject> actual = subjectService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenEmptyListSubjects_whenGetAll_thenNotFoundEntitiesExceptionThrown() {
		given(subjectDao.findAll()).willReturn(new ArrayList<>());

		assertThrows(NotFoundException.class, () -> subjectService.getAll());
	}

	@Test
	void givenExpectedSubject_whenGet_thenEqualSubjectReturned() {
		Subject expected = new Subject(1, "Technology");
		given(subjectDao.get(expected.getId())).willReturn(of(expected));

		Subject actual = subjectService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenOptionalEmptySubject_whenGet_thenNotPresentEntityExceptionThrown() {
		Subject expected = new Subject(5, "Machine Learning");
		given(subjectDao.get(expected.getId())).willReturn(empty());

		assertThrows(NotFoundException.class, () -> subjectService.get(expected.getId()));
	}

	@Test
	void givenSubjectId_whenDelete_thenDeleted() {
		Subject subject = new Subject(7, "Dinamic");
		when(subjectDao.get(subject.getId())).thenReturn(of(subject));

		subjectService.delete(subject.getId());

		verify(subjectDao).delete(subject.getId());
	}

	@Test
	void givenOptionEmptySubject_whenDelete_thenNotFoundExceptionThrown() {
		Subject subject = new Subject(3, "Anatomy");
		when(subjectDao.get(subject.getId())).thenReturn(empty());

		assertThrows(NotFoundException.class, () -> subjectService.delete(subject.getId()));
	}

	@Test
	void givenSubject_whenUpdate_thenUpdated() {
		Subject subject = new Subject(8, "Psychology");
		when(subjectDao.findByName(subject.getName())).thenReturn(of(subject));

		subjectService.update(subject);

		verify(subjectDao).update(subject);
	}

	@Test
	void givenExistingSubject_whenUpdate_thenUpdated() {
		Subject subject = new Subject(7, "Nursing");
		when(subjectDao.findByName(subject.getName())).thenReturn(of(subject));

		subjectService.update(subject);

		verify(subjectDao).update(subject);
	}

	@Test
	void givenNonExistingSubject_whenUpdate_thenNotUpdated() {
		Subject existingSubject = new Subject(7, "Nursing");
		Subject newSubject = new Subject(6, "Nursing");
		when(subjectDao.findByName(newSubject.getName())).thenReturn(of(existingSubject));

		subjectService.update(newSubject);

		verify(subjectDao, never()).update(newSubject);
	}

	@Test
	void givenListOfExistsSubjects_whenFindByTeacherId_thenExpectedListOfSubjectsReturned() {
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
	void givenEmptyListSubjects_whenFindByTeacherId_thenNotFoundEntitiesExceptionThrown() {
		given(subjectDao.findByTeacherId(777)).willReturn(new ArrayList<>());

		assertThrows(NotFoundException.class, () -> subjectService.findByTeacherId(777));
	}

	@Test
	void givenSubject_whenCreate_thenCreated() {
		Subject subject = new Subject(1, "Languages");
		when(subjectDao.findByName(subject.getName())).thenReturn(of(subject));

		subjectService.create(subject);

		verify(subjectDao).create(subject);
	}

	@Test
	void givenNonExistingSubject_whenCreate_thenCreated() {
		Subject existingSubject = new Subject(1, "Phisic");
		Subject newSubject = new Subject(1, "Statistics");
		when(subjectDao.findByName(newSubject.getName())).thenReturn(of(existingSubject));

		subjectService.create(newSubject);

		verify(subjectDao).create(newSubject);
	}

	@Test
	void givenDuplicateNames_whenCreate_thenNotCreatedAndNotUniqueNameExceptionThrown() {
		Subject expected = new Subject(2, "Statistics");
		Subject actual = new Subject(1, "Statistics");
		when(subjectDao.findByName(actual.getName())).thenReturn(empty());

		assertThrows(NotUniqueNameException.class, () -> subjectService.create(expected));

		verify(subjectDao, never()).create(expected);
	}
}
