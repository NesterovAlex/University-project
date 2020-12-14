package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.model.Subject;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

	@Mock
	private SubjectDao subjectDao;

	@InjectMocks
	private SubjectService subjectService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void givenExpectedListOfExistsSubjects_whenGetAll_thenRelevantListOfSubjectsReturned() {
		List<Subject> expected = new ArrayList<>();
		expected.add(new Subject(1, "Literature"));
		expected.add(new Subject(2, "Geography"));
		expected.add(new Subject(1, "Mathematic"));
		given(subjectDao.findAll()).willReturn(expected);

		List<Subject> actual = subjectService.getAll();

		assertEquals(expected, actual);
		verify(subjectDao, times(1)).findAll();
	}

	@Test
	void givenExpectedSubject_whenGet_thenEqualSubjectReturned() {
		Subject expected = new Subject(1, "Technology");
		given(subjectDao.get(expected.getId())).willReturn(expected);

		Subject actual = subjectService.get(expected.getId());

		assertEquals(expected, actual);
		verify(subjectDao, times(1)).get(expected.getId());
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		int expected = 1;

		subjectService.delete(expected);

		verify(subjectDao, times(expected)).delete(expected);
	}

	@Test
	void givenExpectedNameOfExistingSubject_whenUpdate_thenExpectedCountDaoUpdateMethodCall() {
		int expected = 1;
		Subject subject = new Subject(8, "Psychology");
		when(subjectDao.findByName(subject.getName())).thenReturn(subject);

		subjectService.update(subject);

		verify(subjectDao, times(expected)).update(subject);
	}

	@Test
	void givenExistingSubject_whenUpdate_thenExpectedCountDaoUpdateMethodCall() {
		int expected = 1;
		Subject subject = new Subject(7, "Nursing");
		when(subjectDao.findByName(subject.getName())).thenReturn(null);

		subjectService.update(subject);

		verify(subjectDao, timeout(expected)).update(subject);
	}
	
	@Test
	void givenNonExistingSubject_whenUpdate_thenDontCallDaoUpdateMethod() {
		Subject expected = new Subject(6, "Nursing");
		Subject actual = new Subject(7, "Nursing");
		when(subjectDao.findByName(actual.getName())).thenReturn(actual);

		subjectService.update(expected);

		verify(subjectDao, never()).update(expected);
	}

	@Test
	void givenExpectedListOfExistsSubjects_whenFindByTeacherId_thenRelevantListOfSubjectsReturned() {
		int expected = 1;
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(1, "Literature"));
		subjects.add(new Subject(2, "Geography"));
		subjects.add(new Subject(1, "Mathematic"));
		given(subjectDao.findByTeacherId(expected)).willReturn(subjects);

		List<Subject> actual = subjectService.findByTeacherId(expected);

		assertEquals(subjects, actual);
		verify(subjectDao, times(expected)).findByTeacherId(expected);
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenCreate_EqualOfDaoMethodCallReturned() {
		int expected = 1;
		Subject subject = new Subject(1, "Languages");
		when(subjectDao.findByName(subject.getName())).thenReturn(null);

		subjectService.create(subject);

		verify(subjectDao, times(expected)).create(subject);
	}

	@Test
	void givenNonExistingSubject_whenCreate_thenDontCallDaoCreateMethod() {
		int expected = 1;
		Subject subject = new Subject(1, "Statistics");
		when(subjectDao.findByName(subject.getName())).thenReturn(subject);

		subjectService.create(subject);

		verify(subjectDao, timeout(expected)).create(subject);
	}
	
	@Test
	void givenExistingSubject_whenCreate_thenDontCallDaoCreateMethod() {
		Subject expected = new Subject(2, "Statistics");
		Subject actual = new Subject(1, "Statistics");
		when(subjectDao.findByName(actual.getName())).thenReturn(actual);

		subjectService.create(expected);

		verify(subjectDao, never()).create(expected);
	}
}
