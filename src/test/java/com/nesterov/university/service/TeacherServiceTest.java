package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
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
import org.springframework.dao.EmptyResultDataAccessException;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

	@Mock
	private TeacherDao teacherDao;

	@InjectMocks
	private TeacherService teacherService;

	@Spy
	List<Teacher> teachers = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		teachers.add(new Teacher("Petro", "Petrov", LocalDate.of(1999, 9, 9), "Petrovka", "Petro@Petrov", "123456789",
				Gender.MALE));
	}

	@Test
	void givenExpectedListOfExistsTeachers_whenGetAll_thenRelevantListOfTeachersReturned() {
		List<Teacher> expected = new ArrayList<>();
		expected.add(teachers.get(0));
		given(teacherDao.getAll()).willReturn(expected);

		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedTeacher_whenGet_thenEqualTeacherReturned() {
		final Teacher expected = teachers.get(0);
		given(teacherDao.get(anyLong())).willReturn(expected);

		final Teacher actual = teacherService.get(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		doNothing().when(teacherDao).delete(anyLong());

		teacherService.delete(anyLong());

		verify(teacherDao, times(1)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoUpdateMethodCall_whenUpdate_thenEqualOfDaoUpdateMethodCallReturned() {
		int expected = 2;
		Teacher teacher = teachers.get(0);
		doNothing().when(teacherDao).update(any(Teacher.class));

		teacherService.update(teacher);
		teacherService.update(teacher);

		verify(teacherDao, times(expected)).update(teacher);
	}

	@Test
	void givenExpectedListOfExistsTeachers_whenFindBySubjectId_thenRelevantListOfTeachersReturned() {
		List<Teacher> expected = new ArrayList<>();
		expected.add(teachers.get(0));
		given(teacherDao.findBySubjectId(anyLong())).willReturn(expected);

		List<Teacher> actual = teacherService.findBySubjectId(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoCreateMethodCall_whenCreate_EqualOfDaoCreateMethodCallReturned() {
		Teacher teacher = teachers.get(0);
		doThrow(new EmptyResultDataAccessException(1)).when(teacherDao).get(teacher.getId());

		teacherService.create(teachers.get(0));

		verify(teacherDao, times(1)).create(teacher);
	}
}
