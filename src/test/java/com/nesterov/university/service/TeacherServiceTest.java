package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Optional.of;
import static java.util.Optional.empty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.service.exceptions.NotFoundException;
import com.nesterov.university.service.exceptions.NotUniqueAddressException;
import com.nesterov.university.service.exceptions.NotUniqueEmailException;
import com.nesterov.university.service.exceptions.NotUniquePhoneException;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TeacherServiceTest {

	@Mock
	private TeacherDao teacherDao;

	@InjectMocks
	private TeacherService teacherService;

	@Test
	void givenListOfExistsTeachers_whenGetAll_thenExpectedListOfTeachersReturned() {
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
		given(teacherDao.get(expected.getId())).willReturn(of(expected));

		Teacher actual = teacherService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenOptionalEmptyTeacher_whenGet_thenNotFoundExceptionThrown() {
		int id = 555;
		given(teacherDao.get(id)).willReturn(empty());

		assertThrows(NotFoundException.class, () -> teacherService.get(id));
	}

	@Test
	void givenTeacherId_whenDelete_thenDeleted() {
		Teacher teacher = new Teacher("Kenny", "Sam", LocalDate.of(1999, 11, 6), "Sunset", "Kenny@Sam", "584763294",
				Gender.MALE);
		when(teacherDao.get(teacher.getId())).thenReturn(of(teacher));
		teacherService.delete(teacher.getId());

		verify(teacherDao).delete(teacher.getId());
	}

	@Test
	void givenOptionalEmptyTeacher_whenDelete_thenNotFoundExceptionThrown() {
		Teacher teacher = new Teacher("Kreg", "David", LocalDate.of(1985, 7, 26), "Senwood", "Kreg@David", "584736523",
				Gender.MALE);
		when(teacherDao.get(teacher.getId())).thenReturn(empty());

		assertThrows(NotFoundException.class, () -> teacherService.delete(teacher.getId()));
	}

	@Test
	void givenTeacher_whenUpdate_thenUpdated() {
		Teacher teacher = new Teacher("Quinn", "Angelo", LocalDate.of(1993, 3, 3), "Holden", "Quinn@Angelo",
				"3948572395", Gender.MALE);
		when(teacherDao.findByEmail(teacher.getEmail())).thenReturn(of(teacher));
		when(teacherDao.findByPhone(teacher.getPhone())).thenReturn(of(teacher));
		when(teacherDao.findByAddress(teacher.getAddress())).thenReturn(of(teacher));

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
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(empty());

		assertThrows(NotUniqueEmailException.class, () -> teacherService.update(newTeacher));

		verify(teacherDao, never()).update(newTeacher);
	}

	@Test
	void givenSubjectId_whenFindBySubjectId_thenFindTeachers() {
		int subjectId = 1;
		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher("Kerry", "Queen", LocalDate.of(1992, 3, 4), "Holden", "Kerry@Queen", "3948572395",
				Gender.MALE);
		teachers.add(teacher);
		when(teacherDao.findBySubjectId(subjectId)).thenReturn(teachers);

		teacherService.findBySubjectId(subjectId);

		verify(teacherDao).findBySubjectId(subjectId);
	}

	@Test
	void givenEmptyListSubjects_whenFindBySubjectId_thenNotFoundExceptionThrown() {
		int subjectId = 1;
		when(teacherDao.findBySubjectId(subjectId)).thenReturn(new ArrayList<>());

		assertThrows(NotFoundException.class, () -> teacherService.findBySubjectId(subjectId));
	}

	@Test
	void givenTeacher_whenCreate_thenCreated() {
		Teacher teacher = new Teacher("Pedro", "Amari", LocalDate.of(2014, 4, 14), "Lorenzo", "Pedro@Amari",
				"358769341", Gender.FEMALE);
		when(teacherDao.findByEmail(teacher.getEmail())).thenReturn(of(teacher));
		when(teacherDao.findByPhone(teacher.getPhone())).thenReturn(of(teacher));
		when(teacherDao.findByAddress(teacher.getAddress())).thenReturn(of(teacher));

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
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(empty());

		assertThrows(NotUniqueEmailException.class, () -> teacherService.create(newTeacher));

		verify(teacherDao, never()).create(newTeacher);
	}

	@Test
	void givenNewTeacherWithNotUniqueEmail_whenCreate_thenNotCreatedAndNotUniqueEmailExceptionThrown() {
		Teacher existingTeacher = new Teacher("Felix", "Corey", LocalDate.of(2013, 3, 12), "Dakota", "Felix@Corey",
				"358769341", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Felix", "Corey", LocalDate.of(2013, 3, 12), "Dakota", "Felix@Corey",
				"358769341", Gender.MALE);
		newTeacher.setId(7);
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(empty());

		assertThrows(NotUniqueEmailException.class, () -> teacherService.create(newTeacher));
	}

	@Test
	void givenTeacherWithnotUniquePhone_whenCreate_thenNotCreatedAndNotUniquePhoneExceptionThrown() {
		Teacher existingTeacher = new Teacher("Uncle", "Benz", LocalDate.of(2014, 6, 17), "Answood", "Uncle@Benz",
				"358769341", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Ann", "Hataway", LocalDate.of(2013, 3, 12), "Dakota", "Ann@Hataway",
				"358769341", Gender.FEMALE);
		newTeacher.setId(7);
		when(teacherDao.findByPhone(newTeacher.getPhone())).thenReturn(of(newTeacher));
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(of(newTeacher));
		when(teacherDao.findByPhone(newTeacher.getPhone())).thenReturn(empty());

		assertThrows(NotUniquePhoneException.class, () -> teacherService.create(newTeacher));
	}

	@Test
	void givenTeacherWithNotUniqueAddress_whenCreate_thenNotCreatedAndNotUniqueAddressExceptionThrown() {
		Teacher existingTeacher = new Teacher("Crown", "Royal", LocalDate.of(2011, 1, 1), "Denver", "Crown@Royal",
				"4938547463", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Due", "Yang", LocalDate.of(2003, 3, 2), "Denver", "Due@Yang", "203948285",
				Gender.MALE);
		newTeacher.setId(7);
		when(teacherDao.findByPhone(newTeacher.getPhone())).thenReturn(of(newTeacher));
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(of(newTeacher));
		when(teacherDao.findByAddress(newTeacher.getAddress())).thenReturn(empty());

		assertThrows(NotUniqueAddressException.class, () -> teacherService.create(newTeacher));
	}

	@Test
	void givenTeacherWithNotUniqueAddress_whenUpdate_thenNotCreatedAndNotUniqueAddressExceptionThrown() {
		Teacher existingTeacher = new Teacher("Kenny", "Kenn", LocalDate.of(2017, 7, 7), "Krown", "Kenny@Kenn",
				"394857632", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Jorge", "Kloony", LocalDate.of(2012, 3, 13), "Krown", "Jorge@Kloony",
				"3904857632", Gender.MALE);
		newTeacher.setId(7);
		when(teacherDao.findByPhone(newTeacher.getPhone())).thenReturn(of(newTeacher));
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(of(newTeacher));
		when(teacherDao.findByAddress(newTeacher.getAddress())).thenReturn(empty());

		assertThrows(NotUniqueAddressException.class, () -> teacherService.update(newTeacher));
	}

	@Test
	void givenTeacherWithnotUniquePhone_whenUpdate_thenNotCreatedAndNotUniquePhoneExceptionThrown() {
		Teacher existingTeacher = new Teacher("Jonny", "Jack", LocalDate.of(2013, 9, 19), "Jenswill", "Jonny@Jack",
				"358769341", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Henk", "Krenk", LocalDate.of(2012, 11, 4), "Dunkel", "Henk@Krenk",
				"358769341", Gender.FEMALE);
		newTeacher.setId(7);
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(of(newTeacher));
		when(teacherDao.findByPhone(newTeacher.getPhone())).thenReturn(empty());

		assertThrows(NotUniquePhoneException.class, () -> teacherService.update(newTeacher));
	}

	@Test
	void givenNewTeacherWithNotUniqueEmail_whenUpdate_thenNotCreatedAndNotUniqueEmailExceptionThrown() {
		Teacher existingTeacher = new Teacher("Gohn", "Connor", LocalDate.of(2009, 12, 12), "Enswill", "Gohn@Connor",
				"584736285", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Kenny", "Kenn", LocalDate.of(2008, 8, 19), "Kennyswill", "Gohn@Connor",
				"3940587632", Gender.MALE);
		newTeacher.setId(7);
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(empty());

		assertThrows(NotUniqueEmailException.class, () -> teacherService.update(newTeacher));
	}
}
