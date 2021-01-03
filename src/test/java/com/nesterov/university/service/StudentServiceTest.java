package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.stream.Stream.iterate;
import static java.util.Optional.of;
import static java.util.Optional.empty;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.service.exceptions.NotFoundException;
import com.nesterov.university.service.exceptions.NotUniqueAddressException;
import com.nesterov.university.service.exceptions.NotUniqueEmailException;
import com.nesterov.university.service.exceptions.NotUniquePhoneException;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Student;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	@Mock
	private StudentDao studentDao;

	@InjectMocks
	private StudentService studentService;

	@Test
	void giveListOfExistsStudents_whenGetAll_thenRelevantListOfStudentsReturned() {
		Student student = new Student("Jeffrey", "Hector", LocalDate.of(1995, 3, 13), "Shawn", "Jeffrey@Hector",
				"293847563", Gender.MALE);
		List<Student> expected = new ArrayList<>();
		expected.add(student);
		given(studentDao.findAll()).willReturn(expected);

		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenStudent_whenGet_thenExpectedStudentReturned() {
		Student expected = new Student("Lukas", "Amir", LocalDate.of(1994, 4, 11), "Keegan", "Lukas@Amir", "348576983",
				Gender.MALE);
		given(studentDao.get(expected.getId())).willReturn(of(expected));

		Student actual = studentService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenOptionalEmpty_whenGet_thenNotPresentEntityExceptionThrown() {
		Student student = new Student("Lukas", "Amir", LocalDate.of(1994, 4, 11), "Keegan", "Lukas@Amir", "348576983",
				Gender.MALE);
		given(studentDao.get(student.getId())).willReturn(empty());

		assertThrows(NotFoundException.class, () -> studentService.get(student.getId()));
	}

	@Test
	void givenStudentId_whenDelete_thenDeleted() {
		Student student = new Student("Andy", "Mcdowell", LocalDate.of(2000, 1, 1), "Drink", "Andy@Mcdowell",
				"584736251", Gender.MALE);
		when(studentDao.get(student.getId())).thenReturn(of(student));

		studentService.delete(student.getId());

		verify(studentDao).delete(student.getId());
	}

	@Test
	void givenOptionalEmptyStudent_whenDelete_thenNotFoundExceptionThrown() {
		Student student = new Student("John", "Krenck", LocalDate.of(1993, 9, 22), "Doom", "John@Krenck", "594832945",
				Gender.MALE);
		when(studentDao.get(student.getId())).thenReturn(empty());

		assertThrows(NotFoundException.class, () -> studentService.delete(student.getId()));
	}

	@Test
	void givenExistingStudent_whenUpdate_thenUpdated() {
		Student student = new Student("Kyler", "Donovan", LocalDate.of(1995, 5, 15), "Kiev", "Kyler@Donova",
				"483746578", Gender.MALE);
		student.setGroupId(6);
		List<Student> students = new ArrayList<>();
		students.add(student);
		setField(studentService, "maxGroupSize", 30);
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);
		when(studentDao.findByEmail(student.getEmail())).thenReturn(of(student));
		when(studentDao.findByPhone(student.getPhone())).thenReturn(of(student));
		when(studentDao.findByAddress(student.getAddress())).thenReturn(of(student));

		studentService.update(student);

		verify(studentDao).update(student);
	}

	@Test
	void givenNonExistingStudent_whenUpdate_thenNotUpdated() {
		Student existingStudent = new Student("Graham", "Simon", LocalDate.of(1997, 7, 17), "Everett", "Graham@Simon",
				"293847563", Gender.MALE);
		existingStudent.setId(9);
		Student newStudent = new Student("Graham", "Simon", LocalDate.of(1997, 7, 17), "Everett", "Graham@Simon",
				"293847563", Gender.MALE);
		existingStudent.setId(10);
		List<Student> students = new ArrayList<>();
		students.add(newStudent);
		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(of(newStudent));
		when(studentDao.findByGroupId(newStudent.getGroupId())).thenReturn(students);
		when(studentDao.findByPhone(newStudent.getPhone())).thenReturn(of(newStudent));
		when(studentDao.findByAddress(newStudent.getAddress())).thenReturn(of(newStudent));

		studentService.update(newStudent);

		verify(studentDao, never()).update(newStudent);
	}

	@Test
	void givenListOfExistsStudents_whenFindByGroupId_thenExpectedListOfStudentsReturned() {
		Student student = new Student("Clayton", "Braden", LocalDate.of(1999, 6, 14), "Brendan", "Clayton@Braden",
				"3948576238", Gender.MALE);
		List<Student> expected = new ArrayList<>();
		expected.add(student);
		given(studentDao.findByGroupId(student.getId())).willReturn(expected);

		List<Student> actual = studentService.findByGroupId(student.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenStudent_whenCreate_thenCreated() {
		Student student = new Student("Zander", "Jared", LocalDate.of(2019, 02, 15), "Ivanovo", "Zander@Jared",
				"358769341", Gender.FEMALE);
		student.setGroupId(4);
		List<Student> students = new ArrayList<>();
		students.add(student);
		setField(studentService, "maxGroupSize", 30);
		when(studentDao.findByEmail(student.getEmail())).thenReturn(of(student));
		when(studentDao.findByPhone(student.getPhone())).thenReturn(of(student));
		when(studentDao.findByAddress(student.getAddress())).thenReturn(of(student));
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.create(student);

		verify(studentDao).create(student);
	}

	@Test
	void givenEmptyListStudents_whenCreate_thenNotFoundEntitiesExceptionThrown() {
		Student student = new Student("Zander", "Jared", LocalDate.of(2019, 02, 15), "Ivanovo", "Zander@Jared",
				"358769341", Gender.FEMALE);
		List<Student> students = new ArrayList<>();
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);
		when(studentDao.findByEmail(student.getEmail())).thenReturn(of(student));
		when(studentDao.findByPhone(student.getPhone())).thenReturn(of(student));
		when(studentDao.findByAddress(student.getAddress())).thenReturn(of(student));

		assertThrows(NotFoundException.class, () -> studentService.create(student));

		verify(studentDao, never()).create(student);
	}

	@Test
	void givenStudentWithNotUniquePhone_whenCreate_thenNotCreateAndNotUniquePhoneExceptionThrown() {
		Student existingStudent = new Student("Losy", "Dunk", LocalDate.of(2011, 3, 13), "Losevile", "Losy@Dunk",
				"839472834", Gender.FEMALE);
		existingStudent.setId(6);
		Student newStudent = new Student("Losy", "Dunk", LocalDate.of(2011, 3, 13), "Losevile", "Losy@Dunk",
				"839472834", Gender.FEMALE);
		existingStudent.setId(6);
		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(of(newStudent));
		when(studentDao.findByPhone(newStudent.getPhone())).thenReturn(empty());

		assertThrows(NotUniquePhoneException.class, () -> studentService.create(newStudent));

		verify(studentDao, never()).create(newStudent);
	}

	@Test
	void givenStudentWithNotUniqueAddress_whenCreate_thenNotCreateAndNotUniqueAddressExceptionThrown() {
		Student existingStudent = new Student("Shane", "Groov", LocalDate.of(2010, 7, 19), "kenwood", "Shane@Groov",
				"784392856", Gender.FEMALE);
		existingStudent.setId(4);
		Student newStudent = new Student("Roody", "Moor", LocalDate.of(2014, 4, 14), "kenwood", "Roody@Moor",
				"948573624", Gender.FEMALE);
		newStudent.setId(5);

		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(of(newStudent));
		when(studentDao.findByPhone(newStudent.getPhone())).thenReturn(of(newStudent));
		when(studentDao.findByAddress(existingStudent.getAddress())).thenReturn(empty());

		assertThrows(NotUniqueAddressException.class, () -> studentService.create(newStudent));

		verify(studentDao, never()).create(newStudent);
	}

	@Test
	void givenStudentWithNotUniqueEmail_whenCreate_thenNotCreateAndNotUniqueEmailException() {
		Student existingStudent = new Student("Ryker", "Dante", LocalDate.of(2019, 02, 15), "Lane", "Ryker@Dante",
				"358769341", Gender.FEMALE);
		existingStudent.setId(6);
		Student newStudent = new Student("Recul", "Enty", LocalDate.of(2009, 1, 11), "Lane", "Recul@Enty", "358769341",
				Gender.FEMALE);
		existingStudent.setId(7);
		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(empty());

		assertThrows(NotUniqueEmailException.class, () -> studentService.create(newStudent));

		verify(studentDao, never()).create(newStudent);
	}

	@Test
	void givenStudentWithGroupMoreThenThirtyStudentIn_whenCreate_thenNotCreate() {
		Student student = new Student("Kameron", "Elliot", LocalDate.of(2013, 3, 13), "Paxton", "Kameron@Elliot",
				"358769341", Gender.FEMALE);
		List<Student> students = new ArrayList<>();
		Stream.iterate(0, n -> n + 1).limit(50).forEach(x -> students.add(new Student()));
		when(studentDao.findByEmail(student.getEmail())).thenReturn(of(student));
		when(studentDao.findByPhone(student.getPhone())).thenReturn(of(student));
		when(studentDao.findByAddress(student.getAddress())).thenReturn(of(student));
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	void givenStudentWithGroupMoreThenThirtyStudentIn_whenUpdate_thenNotUpdate() {
		Student student = new Student("Rafael", "Dalton", LocalDate.of(2014, 4, 11), "Caiden", "Rafael@Dalton",
				"358769341", Gender.FEMALE);
		List<Student> students = new ArrayList<>();
		iterate(0, n -> n + 1).limit(50).forEach(x -> students.add(new Student()));
		when(studentDao.findByEmail(student.getEmail())).thenReturn(of(student));
		when(studentDao.findByPhone(student.getPhone())).thenReturn(of(student));
		when(studentDao.findByAddress(student.getAddress())).thenReturn(of(student));
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.update(student);

		verify(studentDao, never()).update(student);
	}
}
