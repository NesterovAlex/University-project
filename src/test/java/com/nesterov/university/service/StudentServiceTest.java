package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
		given(studentDao.get(expected.getId())).willReturn(expected);

		Student actual = studentService.get(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	void givenStudentId_whenDelete_thenDeleted() {
		int studentId = 1;

		studentService.delete(studentId);

		verify(studentDao).delete(studentId);
	}

	@Test
	void givenExistingStudent_whenUpdate_thenUpdated() {
		Student student = new Student("Kyler", "Donovan", LocalDate.of(1995, 5, 15), "Kiev", "Kyler@Donova",
				"483746578", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);

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
		when(studentDao.findByEmail(existingStudent.getEmail())).thenReturn(existingStudent);

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
		List<Student> students = new ArrayList<>();
		when(studentDao.findByEmail(student.getEmail())).thenReturn(null);
		when(studentDao.findByPhone(student.getPhone())).thenReturn(null);
		when(studentDao.findByAddress(student.getAddress())).thenReturn(null);
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.create(student);

		verify(studentDao).create(student);
	}

	@Test
	void givenStudentWithNotUniquePhone_whenCreate_thenNotCreate() {
		Student existingStudent = new Student("Losy", "Dunk", LocalDate.of(2011, 3, 13), "Losevile", "Losy@Dunk",
				"839472834", Gender.FEMALE);
		existingStudent.setId(5);
		Student newStudent = new Student("Losy", "Dunk", LocalDate.of(2011, 3, 13), "Losevile", "Losy@Dunk",
				"839472834", Gender.FEMALE);
		existingStudent.setId(6);
		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(null);
		when(studentDao.findByPhone(newStudent.getPhone())).thenReturn(existingStudent);

		studentService.create(newStudent);

		verify(studentDao, never()).create(newStudent);
	}

	@Test
	void givenStudentWithNotUniqueAddress_whenCreate_thenNotCreate() {
		Student existingStudent = new Student("Shane", "Groov", LocalDate.of(2010, 7, 19), "kenwood", "Shane@Groov",
				"784392856", Gender.FEMALE);
		existingStudent.setId(4);
		Student newStudent = new Student("Roody", "Moor", LocalDate.of(2014, 4, 14), "kenwood", "Roody@Moor",
				"948573624", Gender.FEMALE);
		newStudent.setId(5);
		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(null);
		when(studentDao.findByPhone(newStudent.getPhone())).thenReturn(null);
		when(studentDao.findByAddress(existingStudent.getAddress())).thenReturn(existingStudent);

		studentService.create(newStudent);

		verify(studentDao, never()).create(newStudent);
	}

	@Test
	void givenStudentWithNotUniqueEmail_whenCreate_thenNotCreate() {
		Student existingStudent = new Student("Ryker", "Dante", LocalDate.of(2019, 02, 15), "Lane", "Ryker@Dante",
				"358769341", Gender.FEMALE);
		existingStudent.setId(6);
		Student newStudent = new Student("Recul", "Enty", LocalDate.of(2009, 1, 11), "Lane", "Recul@Enty", "358769341",
				Gender.FEMALE);
		existingStudent.setId(7);
		when(studentDao.findByEmail(newStudent.getEmail())).thenReturn(existingStudent);

		studentService.create(newStudent);

		verify(studentDao, never()).create(newStudent);
	}

	@Test
	void givenStudentWithGroupMoreThenThirtyStudentIn_whenCreate_thenNotCreate() {
		Student student = new Student("Kameron", "Elliot", LocalDate.of(2013, 3, 13), "Paxton", "Kameron@Elliot",
				"358769341", Gender.FEMALE);
		List<Student> students = new ArrayList<>();
		Stream.iterate(0, n -> n + 1).limit(50).forEach(x -> students.add(new Student()));
		when(studentDao.findByEmail(student.getEmail())).thenReturn(null);
		when(studentDao.findByPhone(student.getPhone())).thenReturn(null);
		when(studentDao.findByAddress(student.getAddress())).thenReturn(null);
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	void givenStudentWithGroupMoreThenThirtyStudentIn_whenUpdate_thenNotUpdate() {
		Student student = new Student("Rafael", "Dalton", LocalDate.of(2014, 4, 11), "Caiden", "Rafael@Dalton",
				"358769341", Gender.FEMALE);
		List<Student> students = new ArrayList<>();
		Stream.iterate(0, n -> n + 1).limit(50).forEach(x -> students.add(new Student()));
		when(studentDao.findByEmail(student.getEmail())).thenReturn(null);
		when(studentDao.findByPhone(student.getPhone())).thenReturn(null);
		when(studentDao.findByAddress(student.getAddress())).thenReturn(null);
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.update(student);

		verify(studentDao, never()).update(student);
	}
}
