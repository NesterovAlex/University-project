package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
		Subject expected = new Subject(1, "Literature");
		given(subjectDao.get(anyLong())).willReturn(expected);

		Subject actual = subjectService.get(anyLong());

		assertEquals(expected, actual);
		verify(subjectDao, times(1)).get(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		int expected = 1;

		subjectService.delete(anyLong());

		verify(subjectDao, times(expected)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoUpdateMethodCall_whenUpdate_thenEqualOfDaoUpdateMethodCallReturned() {
		int expected = 2;
		Subject subject = new Subject(8, "Literature");

		subjectService.update(subject);
		subjectService.update(subject);

		verify(subjectDao, times(expected)).update(subject);
	}

	@Test
	void givenExpectedListOfExistsSubjects_whenFindByTeacherId_thenRelevantListOfSubjectsReturned() {
		List<Subject> expected = new ArrayList<>();
		expected.add(new Subject(1, "Literature"));
		expected.add(new Subject(2, "Geography"));
		expected.add(new Subject(1, "Mathematic"));
		given(subjectDao.findByTeacherId(anyLong())).willReturn(expected);

		List<Subject> actual = subjectService.findByTeacherId(anyLong());

		assertEquals(expected, actual);
		verify(subjectDao, times(1)).findByTeacherId(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenCreate_EqualOfDaoMethodCallReturned() {
		int expected = 1;
		Subject subject = new Subject(1, "Literature");

		subjectService.create(subject);

		verify(subjectDao, times(expected)).create(subject);
	}

	@Test
	void givenExistingSubject_whenCreate_thenDontCallDaoCreateMethod() {
		Subject subject = new Subject(1, "Literature");
		when(subjectDao.findByName(subject.getName())).thenReturn(subject);

		subjectService.create(subject);

		verify(subjectDao, never()).create(subject);
	}
}
