package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.EmptyResultDataAccessException;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.model.Subject;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SubjectServiceTest {

	@Mock
	private SubjectDao subjectDao;

	@InjectMocks
	private SubjectService subjectService;

	@Spy
	List<Subject> subjects = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		subjects.add(new Subject(1, "Literature"));
	}

	@Test
	void givenExpectedListOfExistsSubjects_whenFindAll_thenRelevantListOfSubjectsReturned() {
		List<Subject> expected = new ArrayList<>();
		expected.add(new Subject(1, "Literature"));
		expected.add(new Subject(2, "Geography"));
		expected.add(new Subject(1, "Mathematic"));
		given(subjectDao.getAll()).willReturn(expected);

		List<Subject> actual = subjectService.findAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedSubject_whenGetSubject_thenEqualSubjectReturned() {
		final Subject expected = new Subject(1, "Literature");
		given(subjectDao.get(anyLong())).willReturn(expected);

		final Subject actual = subjectService.get(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenDeleteSubject_thenEqualOfDaoMethodCallReturned() {
		doNothing().when(subjectDao).delete(anyLong());

		subjectService.delete(anyLong());

		verify(subjectDao, times(1)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenUpdateSubject_thenEqualOfDaoMethodCallReturned() {
		Subject expected = new Subject(8, "Literature");
		doNothing().when(subjectDao).update(any(Subject.class));
		
		subjectService.update(expected);
		subjectService.update(expected);
		
		verify(subjectDao, times(2)).update(expected);
	}

	@Test
	void givenExpectedListOfExistsSubjects_whenfindAllByTeacher_thenRelevantListOfSubjectsReturned() {
		List<Subject> expected = new ArrayList<>();
		expected.add(new Subject(1, "Literature"));
		expected.add(new Subject(2, "Geography"));
		expected.add(new Subject(1, "Mathematic"));
		given(subjectDao.findByTeacherId(anyLong())).willReturn(expected);

		List<Subject> actual = subjectService.findByTeacherId(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenCreate_EqualOfDaoMethodCallReturned() {
		Subject subject = new Subject(1, "Literature");
		doThrow(new EmptyResultDataAccessException(1)).when(subjectDao).get(1);

		subjectService.create(subjects.get(0));

		verify(subjectDao, times(1)).create(subject);
	}

}
