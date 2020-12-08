package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
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
		Teacher teacher = new Teacher("Fabian", "Zayden", LocalDate.of(1992, 4, 3), "Brennan", "Fabian@Zayde",
				"594857632", Gender.MALE);
		List<Teacher> expected = new ArrayList<>();
		expected.add(teacher);
		given(teacherDao.findAll()).willReturn(expected);

		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedTeacher_whenGet_thenEqualTeacherReturned() {
		Teacher expected = new Teacher("Anderson", "Roberto", LocalDate.of(1991, 11, 10), "Reid", "Anderson@Roberto",
				"938472634", Gender.MALE);
		given(teacherDao.get(expected.getId())).willReturn(expected);

		Teacher actual = teacherService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		int expected = 1;

		teacherService.delete(expected);

		verify(teacherDao, times(expected)).delete(expected);
	}

	@Test
	void givenExpectedCountOfDaoUpdateMethodCall_whenUpdate_thenEqualOfDaoUpdateMethodCallReturned() {
		int expected = 1;
		Teacher teacher = new Teacher("Quinn", "Angelo", LocalDate.of(1993, 3, 3), "Holden", "Quinn@Angelo",
				"3948572395", Gender.MALE);
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher);
		when(teacherDao.findByEmail(teacher.getEmail())).thenReturn(teachers);
		when(teacherDao.findByPhone(teacher.getPhone())).thenReturn(teachers);
		when(teacherDao.findByAddress(teacher.getAddress())).thenReturn(teachers);

		teacherService.update(teacher);

		verify(teacherDao, times(expected)).update(teacher);
	}

	@Test
	void givenNonExistingTeacher_whenUpdate_thenDaoUpdateMethodDontCall() {
		Teacher teacher = new Teacher("Cruz", "Derrick", LocalDate.of(1995, 5, 5), "Finn", "Cruz@Derrick", "492034857",
				Gender.MALE);
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher);
		when(teacherDao.findByEmail(teacher.getEmail())).thenReturn(null);

		teacherService.update(teacher);

		verify(teacherDao, never()).update(teacher);
	}

	@Test
	void givenExpectedCountCallsOfFindBySubjectMethod_whenFindBySubjectId_thenCountReturned() {
		int expected = 1;

		teacherService.findBySubjectId(expected);

		verify(teacherDao, times(expected)).findBySubjectId(expected);
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenCreate_EqualOfDaoMethodCallReturned() {
		int expected = 1;
		Teacher teacher = new Teacher("Pedro", "Amari", LocalDate.of(2014, 4, 14), "Lorenzo", "Pedro@Amari",
				"358769341", Gender.FEMALE);
		when(teacherDao.findByEmail(teacher.getEmail())).thenReturn(null);
		when(teacherDao.findByPhone(teacher.getPhone())).thenReturn(null);
		when(teacherDao.findByAddress(teacher.getAddress())).thenReturn(null);

		teacherService.create(teacher);

		verify(teacherDao, times(expected)).create(teacher);
	}

	@Test
	void givenExistingTeacher_whenCreate_thenDaoCreateMethodDontCall() {
		Teacher teacher = new Teacher("Felix", "Corey", LocalDate.of(2013, 3, 12), "Dakota", "Felix@Corey", "358769341",
				Gender.FEMALE);

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}
}
