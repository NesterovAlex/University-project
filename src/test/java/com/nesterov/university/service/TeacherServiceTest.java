package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

	@Mock
	private TeacherDao teacherDao;

	@InjectMocks
	private TeacherService teacherService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void givenExpectedListOfExistsTeachers_whenGetAll_thenRelevantListOfTeachersReturned() {
		Teacher teacher = new Teacher("Petro", "Petrov", LocalDate.of(1999, 9, 9), "Petrovka", "Petro@Petrov",
				"123456789", Gender.MALE);
		List<Teacher> expected = new ArrayList<>();
		expected.add(teacher);
		given(teacherDao.findAll()).willReturn(expected);

		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedTeacher_whenGet_thenEqualTeacherReturned() {
		Teacher expected = new Teacher("Petro", "Petrov", LocalDate.of(1999, 9, 9), "Petrovka", "Petro@Petrov",
				"123456789", Gender.MALE);
		given(teacherDao.get(anyLong())).willReturn(expected);

		final Teacher actual = teacherService.get(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		int expected = 1;

		teacherService.delete(anyLong());

		verify(teacherDao, times(expected)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoUpdateMethodCall_whenUpdate_thenEqualOfDaoUpdateMethodCallReturned() {
		int expected = 2;
		Teacher teacher = new Teacher("Petro", "Petrov", LocalDate.of(1999, 9, 9), "Petrovka", "Petro@Petrov",
				"123456789", Gender.MALE);
		when(teacherDao.findByEmail(any(String.class))).thenReturn(null);
		when(teacherDao.findByPhone(any(String.class))).thenReturn(null);
		when(teacherDao.findByAddress(any(String.class))).thenReturn(null);

		teacherService.update(teacher);
		teacherService.update(teacher);

		verify(teacherDao, times(expected)).update(teacher);
	}

	@Test
	void givenExpectedCountCallsOfFindBySubjectMethod_whenFindBySubjectId_thenCountReturned() {
		int expected = 1;
		teacherService.findBySubjectId(anyLong());

		verify(teacherDao, times(expected)).findBySubjectId(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenCreate_EqualOfDaoMethodCallReturned() {
		int expected = 1;
		Teacher teacher = new Teacher("Ivanka", "Ivanova", LocalDate.of(2019, 02, 15), "Ivanovo", "ivanka@ivanova",
				"358769341", Gender.FEMALE);
		when(teacherDao.findByEmail(any(String.class))).thenReturn(null);
		when(teacherDao.findByPhone(any(String.class))).thenReturn(null);
		when(teacherDao.findByAddress(any(String.class))).thenReturn(null);

		teacherService.create(teacher);

		verify(teacherDao, times(expected)).create(teacher);
	}

	@Test
	void givenExistingTeacher_whenCreate_thenDaoCreateMethodDontCall() {
		Teacher teacher = new Teacher("Ivanka", "Ivanova", LocalDate.of(2019, 02, 15), "Ivanovo", "ivanka@ivanova",
				"358769341", Gender.FEMALE);

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}
}
