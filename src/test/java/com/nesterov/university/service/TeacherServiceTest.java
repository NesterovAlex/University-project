package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Optional.ofNullable;
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
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotUniqueAddressException;
import com.nesterov.university.dao.exceptions.NotUniqueEmailException;
import com.nesterov.university.dao.exceptions.NotUniquePhoneException;
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
	void givenListOfExistsTeachers_whenGetAll_thenExpectedListOfTeachersReturned() throws NotFoundEntitiesException {
		Teacher teacher = new Teacher("Fabian", "Zayden", LocalDate.of(1992, 4, 3), "Brennan", "Fabian@Zayde",
				"594857632", Gender.MALE);
		List<Teacher> expected = new ArrayList<>();
		expected.add(teacher);
		given(teacherDao.findAll()).willReturn(expected);

		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenEmptyListTeachers_whenGetAll_thenNotFoundEntitiesExceptionThrown() throws NotFoundEntitiesException {
		given(teacherDao.findAll()).willReturn(new ArrayList<>());

		assertThrows(NotFoundEntitiesException.class, () -> teacherService.getAll());
	}

	@Test
	void givenExpectedTeacher_whenGet_thenEqualTeacherReturned() throws NotPresentEntityException {
		Teacher expected = new Teacher("Anderson", "Roberto", LocalDate.of(1991, 11, 10), "Reid", "Anderson@Roberto",
				"938472634", Gender.MALE);
		given(teacherDao.get(expected.getId())).willReturn(ofNullable(expected));

		Teacher actual = teacherService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenOptionalEmptyTeacher_whenGet_thenNotPresentEntityExceptionThrown() throws NotPresentEntityException {
		int id = 555;
		given(teacherDao.get(id)).willReturn(empty());

		assertThrows(NotPresentEntityException.class, () -> teacherService.get(id));
	}

	@Test
	void givenTeacherId_whenDelete_thenDeleted() throws NotDeleteException {
		int teacherId = 1;

		teacherService.delete(teacherId);

		verify(teacherDao).delete(teacherId);
	}

	@Test
	void givenTeacher_whenUpdate_thenUpdated()
			throws NotUniqueEmailException, NotUniquePhoneException, NotUniqueAddressException {
		Teacher teacher = new Teacher("Quinn", "Angelo", LocalDate.of(1993, 3, 3), "Holden", "Quinn@Angelo",
				"3948572395", Gender.MALE);

		teacherService.update(teacher);

		verify(teacherDao).update(teacher);
	}

	@Test
	void givenNonExistingTeacher_whenUpdate_thenNotUpdated() throws NotUniqueEmailException {
		Teacher existingTeacher = new Teacher("Cruz", "Derrick", LocalDate.of(1995, 5, 5), "Finn", "Cruz@Derrick",
				"492034857", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Cruz", "Derrick", LocalDate.of(1995, 5, 5), "Finn", "Cruz@Derrick",
				"492034857", Gender.MALE);
		existingTeacher.setId(7);
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(ofNullable(existingTeacher));

		assertThrows(NotUniqueEmailException.class, () -> teacherService.update(newTeacher));

		verify(teacherDao, never()).update(newTeacher);
	}

	@Test
	void givenSubjectId_whenFindBySubjectId_thenFindTeachers() throws NotFoundEntitiesException {
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
	void givenEmptyListSubjects_whenFindBySubjectId_thenNotFoundEntitiesExceptionThrown()
			throws NotFoundEntitiesException {
		int subjectId = 1;
		when(teacherDao.findBySubjectId(subjectId)).thenReturn(new ArrayList<>());

		assertThrows(NotFoundEntitiesException.class, () -> teacherService.findBySubjectId(subjectId));
	}

	@Test
	void givenTeacher_whenCreate_thenCreated()
			throws NotUniqueEmailException, NotCreateException, NotUniquePhoneException, NotUniqueAddressException {
		Teacher teacher = new Teacher("Pedro", "Amari", LocalDate.of(2014, 4, 14), "Lorenzo", "Pedro@Amari",
				"358769341", Gender.FEMALE);
		when(teacherDao.findByEmail(teacher.getEmail())).thenReturn(empty());
		when(teacherDao.findByPhone(teacher.getPhone())).thenReturn(empty());
		when(teacherDao.findByAddress(teacher.getAddress())).thenReturn(empty());

		teacherService.create(teacher);

		verify(teacherDao).create(teacher);
	}

	@Test
	void givenExistingTeacher_whenCreate_thenNotCreated() throws NotUniqueEmailException, NotCreateException {
		Teacher existingTeacher = new Teacher("Felix", "Corey", LocalDate.of(2013, 3, 12), "Dakota", "Felix@Corey",
				"358769341", Gender.FEMALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Felix", "Corey", LocalDate.of(2013, 3, 12), "Dakota", "Felix@Corey",
				"358769341", Gender.FEMALE);
		newTeacher.setId(7);
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(ofNullable(existingTeacher));

		assertThrows(NotUniqueEmailException.class, () -> teacherService.create(newTeacher));

		verify(teacherDao, never()).create(newTeacher);
	}

	@Test
	void givenNewTeacherWithNotUniqueEmail_whenCreate_thenNotCreatedAndNotUniqueEmailExceptionThrown()
			throws NotUniqueEmailException, NotCreateException {
		Teacher existingTeacher = new Teacher("Felix", "Corey", LocalDate.of(2013, 3, 12), "Dakota", "Felix@Corey",
				"358769341", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Felix", "Corey", LocalDate.of(2013, 3, 12), "Dakota", "Felix@Corey",
				"358769341", Gender.MALE);
		newTeacher.setId(7);
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(ofNullable(existingTeacher));

		assertThrows(NotUniqueEmailException.class, () -> teacherService.create(newTeacher));
	}

	@Test
	void givenTeacherWithnotUniquePhone_whenCreate_thenNotCreatedAndNotUniquePhoneExceptionThrown()
			throws NotUniqueEmailException, NotCreateException {
		Teacher existingTeacher = new Teacher("Uncle", "Benz", LocalDate.of(2014, 6, 17), "Answood", "Uncle@Benz",
				"358769341", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Ann", "Hataway", LocalDate.of(2013, 3, 12), "Dakota", "Ann@Hataway",
				"358769341", Gender.FEMALE);
		newTeacher.setId(7);
		when(teacherDao.findByPhone(newTeacher.getPhone())).thenReturn(ofNullable(existingTeacher));

		assertThrows(NotUniquePhoneException.class, () -> teacherService.create(newTeacher));
	}

	@Test
	void givenTeacherWithNotUniqueAddress_whenCreate_thenNotCreatedAndNotUniqueAddressExceptionThrown()
			throws NotUniqueEmailException, NotCreateException {
		Teacher existingTeacher = new Teacher("Crown", "Royal", LocalDate.of(2011, 1, 1), "Denver", "Crown@Royal",
				"4938547463", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Due", "Yang", LocalDate.of(2003, 3, 2), "Denver", "Due@Yang", "203948285",
				Gender.MALE);
		newTeacher.setId(7);
		when(teacherDao.findByAddress(newTeacher.getAddress())).thenReturn(ofNullable(existingTeacher));

		assertThrows(NotUniqueAddressException.class, () -> teacherService.create(newTeacher));
	}

	@Test
	void givenTeacherWithNotUniqueAddress_whenUpdate_thenNotCreatedAndNotUniqueAddressExceptionThrown()
			throws NotUniqueEmailException, NotCreateException {
		Teacher existingTeacher = new Teacher("Kenny", "Kenn", LocalDate.of(2017, 7, 7), "Krown", "Kenny@Kenn",
				"394857632", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Jorge", "Kloony", LocalDate.of(2012, 3, 13), "Krown", "Jorge@Kloony",
				"3904857632", Gender.MALE);
		newTeacher.setId(7);
		when(teacherDao.findByAddress(newTeacher.getAddress())).thenReturn(ofNullable(existingTeacher));

		assertThrows(NotUniqueAddressException.class, () -> teacherService.update(newTeacher));
	}

	@Test
	void givenTeacherWithnotUniquePhone_whenUpdate_thenNotCreatedAndNotUniquePhoneExceptionThrown()
			throws NotUniqueEmailException, NotCreateException {
		Teacher existingTeacher = new Teacher("Jonny", "Jack", LocalDate.of(2013, 9, 19), "Jenswill", "Jonny@Jack",
				"358769341", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Henk", "Krenk", LocalDate.of(2012, 11, 4), "Dunkel", "Henk@Krenk",
				"358769341", Gender.FEMALE);
		newTeacher.setId(7);
		when(teacherDao.findByPhone(newTeacher.getPhone())).thenReturn(ofNullable(existingTeacher));

		assertThrows(NotUniquePhoneException.class, () -> teacherService.update(newTeacher));
	}

	@Test
	void givenNewTeacherWithNotUniqueEmail_whenUpdate_thenNotCreatedAndNotUniqueEmailExceptionThrown()
			throws NotUniqueEmailException, NotCreateException {
		Teacher existingTeacher = new Teacher("Gohn", "Connor", LocalDate.of(2009, 12, 12), "Enswill", "Gohn@Connor",
				"584736285", Gender.MALE);
		existingTeacher.setId(6);
		Teacher newTeacher = new Teacher("Kenny", "Kenn", LocalDate.of(2008, 8, 19), "Kennyswill", "Gohn@Connor",
				"3940587632", Gender.MALE);
		newTeacher.setId(7);
		when(teacherDao.findByEmail(newTeacher.getEmail())).thenReturn(ofNullable(existingTeacher));

		assertThrows(NotUniqueEmailException.class, () -> teacherService.update(newTeacher));
	}
}
