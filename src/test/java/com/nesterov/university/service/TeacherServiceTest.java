package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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
	void givenExpectedIdOfTeacher_whenDelete_thenDeleted() {
		int expected = 1;

		teacherService.delete(expected);

		verify(teacherDao).delete(expected);
	}

	@Test
	void givenExpectedTeacher_whenUpdate_thenUpdated() {
		Teacher teacher = new Teacher("Quinn", "Angelo", LocalDate.of(1993, 3, 3), "Holden", "Quinn@Angelo",
				"3948572395", Gender.MALE);

		teacherService.update(teacher);

		verify(teacherDao).update(teacher);
	}

	@Test
	void givenNonExistingTeacher_whenUpdate_thenNotUpdated() {
		Teacher existingTeacher = new Teacher("Cruz", "Derrick", LocalDate.of(1995, 5, 5), "Finn", "Cruz@Derrick",
				"492034857", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Cruz", "Derrick", LocalDate.of(1995, 5, 5), "Finn", "Cruz@Derrick",
				"492034857", Gender.MALE);
		existingTeacher.setId(7);
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(existingTeacher);

		teacherService.update(newTeacher);

		verify(teacherDao, never()).update(newTeacher);
	}

	@Test
	void givenExpectedsubjectId_whenFindBySubjectId_thenFindTeachers() {
		int expected = 1;

		teacherService.findBySubjectId(expected);

		verify(teacherDao).findBySubjectId(expected);
	}

	@Test
	void givenExpectedTeacher_whenCreate_thenCreated() {
		Teacher teacher = new Teacher("Pedro", "Amari", LocalDate.of(2014, 4, 14), "Lorenzo", "Pedro@Amari",
				"358769341", Gender.FEMALE);
		when(teacherDao.findByEmail(teacher.getEmail())).thenReturn(null);
		when(teacherDao.findByPhone(teacher.getPhone())).thenReturn(null);
		when(teacherDao.findByAddress(teacher.getAddress())).thenReturn(null);

		teacherService.create(teacher);

		verify(teacherDao).create(teacher);
	}

	@Test
	void givenExistingTeacher_whenCreate_thenNotCreated() {
		Teacher existingTeacher = new Teacher("Felix", "Corey", LocalDate.of(2013, 3, 12), "Dakota", "Felix@Corey",
				"358769341", Gender.FEMALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Felix", "Corey", LocalDate.of(2013, 3, 12), "Dakota", "Felix@Corey",
				"358769341", Gender.FEMALE);
		newTeacher.setId(7);
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(existingTeacher);

		teacherService.create(newTeacher);

		verify(teacherDao, never()).create(newTeacher);
	}
}
