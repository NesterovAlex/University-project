package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.stream.Stream.iterate;
import static java.util.Optional.ofNullable;
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
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotUniqueAddressException;
import com.nesterov.university.dao.exceptions.NotUniqueEmailException;
import com.nesterov.university.dao.exceptions.NotUniquePhoneException;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Student;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	@Mock
	private StudentDao studentDao;

	@InjectMocks
	private StudentService studentService;

	@Test
	void giveListOfExistsStudents_whenGetAll_thenRelevantListOfStudentsReturned() throws NotFoundEntitiesException {
		Student student = new Student("Jeffrey", "Hector", LocalDate.of(1995, 3, 13), "Shawn", "Jeffrey@Hector",
				"293847563", Gender.MALE);
		List<Student> expected = new ArrayList<>();
		expected.add(student);
		given(studentDao.findAll()).willReturn(expected);

		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenEmptyListStudents_whenGetAll_thenNotFoundEntitiesExceptionThrown() throws NotFoundEntitiesException {
		given(studentDao.findAll()).willReturn(new ArrayList<>());

		assertThrows(NotFoundEntitiesException.class, () -> studentService.getAll());
	}

	@Test
	void givenStudent_whenGet_thenExpectedStudentReturned() throws EntityNotFoundException, NotPresentEntityException {
		Student expected = new Student("Lukas", "Amir", LocalDate.of(1994, 4, 11), "Keegan", "Lukas@Amir", "348576983",
				Gender.MALE);
		given(studentDao.get(expected.getId())).willReturn(ofNullable(expected));

		Student actual = studentService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenOptionalEmpty_whenGet_thenNotPresentEntityExceptionThrown() throws NotPresentEntityException {
		Student student = new Student("Lukas", "Amir", LocalDate.of(1994, 4, 11), "Keegan", "Lukas@Amir", "348576983",
				Gender.MALE);
		given(studentDao.get(student.getId())).willReturn(empty());

		assertThrows(NotPresentEntityException.class, () -> studentService.get(student.getId()));
	}

	@Test
	void givenStudentId_whenDelete_thenDeleted() throws NotDeleteException {
		int studentId = 1;

		studentService.delete(studentId);

		verify(studentDao).delete(studentId);
	}

	@Test
	void givenExistingStudent_whenUpdate_thenUpdated() throws NotFoundEntitiesException, NotUniqueAddressException,
			NotUniqueEmailException, NotUniquePhoneException {
		Student student = new Student("Kyler", "Donovan", LocalDate.of(1995, 5, 15), "Kiev", "Kyler@Donova",
				"483746578", Gender.MALE);
		student.setGroupId(6);
		List<Student> students = new ArrayList<>();
		students.add(student);
		setField(studentService, "maxGroupSize", 30);
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.update(student);

		verify(studentDao).update(student);
	}

	@Test
	void givenNonExistingStudent_whenUpdate_thenNotUpdated() throws NotFoundEntitiesException,
			NotUniqueAddressException, NotUniqueEmailException, NotUniquePhoneException {
		Student existingStudent = new Student("Graham", "Simon", LocalDate.of(1997, 7, 17), "Everett", "Graham@Simon",
				"293847563", Gender.MALE);
		existingStudent.setId(9);
		Student newStudent = new Student("Graham", "Simon", LocalDate.of(1997, 7, 17), "Everett", "Graham@Simon",
				"293847563", Gender.MALE);
		existingStudent.setId(10);
		List<Student> students = new ArrayList<>();
		students.add(newStudent);
		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(empty());
		when(studentDao.findByGroupId(newStudent.getGroupId())).thenReturn(students);

		studentService.update(newStudent);

		verify(studentDao, never()).update(newStudent);
	}

	@Test
	void givenListOfExistsStudents_whenFindByGroupId_thenExpectedListOfStudentsReturned()
			throws NotFoundEntitiesException {
		Student student = new Student("Clayton", "Braden", LocalDate.of(1999, 6, 14), "Brendan", "Clayton@Braden",
				"3948576238", Gender.MALE);
		List<Student> expected = new ArrayList<>();
		expected.add(student);
		given(studentDao.findByGroupId(student.getId())).willReturn(expected);

		List<Student> actual = studentService.findByGroupId(student.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenStudent_whenCreate_thenCreated() throws NotFoundEntitiesException, NotCreateException,
			NotUniqueAddressException, NotUniqueEmailException, NotUniquePhoneException {
		Student student = new Student("Zander", "Jared", LocalDate.of(2019, 02, 15), "Ivanovo", "Zander@Jared",
				"358769341", Gender.FEMALE);
		student.setGroupId(4);
		List<Student> students = new ArrayList<>();
		students.add(student);
		setField(studentService, "maxGroupSize", 30);
		when(studentDao.findByEmail(student.getEmail())).thenReturn(empty());
		when(studentDao.findByPhone(student.getPhone())).thenReturn(empty());
		when(studentDao.findByAddress(student.getAddress())).thenReturn(empty());
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.create(student);

		verify(studentDao).create(student);
	}

	@Test
	void givenEmptyListStudents_whenCreate_thenNotFoundEntitiesExceptionThrown()
			throws NotFoundEntitiesException, NotCreateException {
		Student student = new Student("Zander", "Jared", LocalDate.of(2019, 02, 15), "Ivanovo", "Zander@Jared",
				"358769341", Gender.FEMALE);
		List<Student> students = new ArrayList<>();
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		assertThrows(NotFoundEntitiesException.class, () -> studentService.create(student));

		verify(studentDao, never()).create(student);
	}

	@Test
	void givenStudentWithNotUniquePhone_whenCreate_thenNotCreateAndNotUniquePhoneExceptionThrown()
			throws NotCreateException, NotFoundEntitiesException, NotUniqueAddressException, NotUniqueEmailException,
			NotUniquePhoneException {
		Student existingStudent = new Student("Losy", "Dunk", LocalDate.of(2011, 3, 13), "Losevile", "Losy@Dunk",
				"839472834", Gender.FEMALE);
		existingStudent.setId(5);
		Student newStudent = new Student("Losy", "Dunk", LocalDate.of(2011, 3, 13), "Losevile", "Losy@Dunk",
				"839472834", Gender.FEMALE);
		existingStudent.setId(6);
		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(empty());
		when(studentDao.findByPhone(newStudent.getPhone())).thenReturn(ofNullable(existingStudent));

		assertThrows(NotUniquePhoneException.class, () -> studentService.create(newStudent));

		verify(studentDao, never()).create(newStudent);
	}

	@Test
	void givenStudentWithNotUniqueAddress_whenCreate_thenNotCreateAndNotUniqueAddressExceptionThrown()
			throws NotCreateException, NotFoundEntitiesException, NotUniqueAddressException, NotUniqueEmailException,
			NotUniquePhoneException {
		Student existingStudent = new Student("Shane", "Groov", LocalDate.of(2010, 7, 19), "kenwood", "Shane@Groov",
				"784392856", Gender.FEMALE);
		existingStudent.setId(4);
		Student newStudent = new Student("Roody", "Moor", LocalDate.of(2014, 4, 14), "kenwood", "Roody@Moor",
				"948573624", Gender.FEMALE);
		newStudent.setId(5);
		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(empty());
		when(studentDao.findByPhone(newStudent.getPhone())).thenReturn(empty());
		when(studentDao.findByAddress(existingStudent.getAddress())).thenReturn(ofNullable(existingStudent));

		assertThrows(NotUniqueAddressException.class, () -> studentService.create(newStudent));

		verify(studentDao, never()).create(newStudent);
	}

	@Test
	void givenStudentWithNotUniqueEmail_whenCreate_thenNotCreateAndNotUniqueEmailException()
			throws NotCreateException, NotFoundEntitiesException, NotUniqueAddressException, NotUniqueEmailException {
		Student existingStudent = new Student("Ryker", "Dante", LocalDate.of(2019, 02, 15), "Lane", "Ryker@Dante",
				"358769341", Gender.FEMALE);
		existingStudent.setId(6);
		Student newStudent = new Student("Recul", "Enty", LocalDate.of(2009, 1, 11), "Lane", "Recul@Enty", "358769341",
				Gender.FEMALE);
		existingStudent.setId(7);
		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(ofNullable(existingStudent));

		assertThrows(NotUniqueEmailException.class, () -> studentService.create(newStudent));

		verify(studentDao, never()).create(newStudent);
	}

	@Test
	void givenStudentWithGroupMoreThenThirtyStudentIn_whenCreate_thenNotCreate() throws NotCreateException,
			NotFoundEntitiesException, NotUniqueAddressException, NotUniqueEmailException, NotUniquePhoneException {
		Student student = new Student("Kameron", "Elliot", LocalDate.of(2013, 3, 13), "Paxton", "Kameron@Elliot",
				"358769341", Gender.FEMALE);
		List<Student> students = new ArrayList<>();
		Stream.iterate(0, n -> n + 1).limit(50).forEach(x -> students.add(new Student()));
		when(studentDao.findByEmail(student.getEmail())).thenReturn(empty());
		when(studentDao.findByPhone(student.getPhone())).thenReturn(empty());
		when(studentDao.findByAddress(student.getAddress())).thenReturn(empty());
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	void givenStudentWithGroupMoreThenThirtyStudentIn_whenUpdate_thenNotUpdate() throws NotCreateException,
			NotFoundEntitiesException, NotUniqueAddressException, NotUniqueEmailException, NotUniquePhoneException {
		Student student = new Student("Rafael", "Dalton", LocalDate.of(2014, 4, 11), "Caiden", "Rafael@Dalton",
				"358769341", Gender.FEMALE);
		List<Student> students = new ArrayList<>();
		iterate(0, n -> n + 1).limit(50).forEach(x -> students.add(new Student()));
		when(studentDao.findByEmail(student.getEmail())).thenReturn(empty());
		when(studentDao.findByPhone(student.getPhone())).thenReturn(empty());
		when(studentDao.findByAddress(student.getAddress())).thenReturn(empty());
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.update(student);

		verify(studentDao, never()).update(student);
	}
}
