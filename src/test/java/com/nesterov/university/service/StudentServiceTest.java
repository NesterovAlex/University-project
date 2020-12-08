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
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	void givenExpectedListOfExistsStudents_whenGetAll_thenRelevantListOfStudentsReturned() {
		Student student = new Student("Jeffrey", "Hector", LocalDate.of(1995, 3, 13), "Shawn", "Jeffrey@Hector",
				"293847563", Gender.MALE);
		List<Student> expected = new ArrayList<>();
		expected.add(student);
		given(studentDao.findAll()).willReturn(expected);

		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
		verify(studentDao, times(1)).findAll();
	}

	@Test
	void givenExpectedStudent_whenGet_thenEqualStudentReturned() {
		Student expected = new Student("Lukas", "Amir", LocalDate.of(1994, 4, 11), "Keegan", "Lukas@Amir", "348576983",
				Gender.MALE);
		given(studentDao.get(expected.getId())).willReturn(expected);

		Student actual = studentService.get(expected.getId());

		assertEquals(expected, actual);
		verify(studentDao, times(1)).get(expected.getId());
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		int expected = 1;

		studentService.delete(expected);

		verify(studentDao, times(expected)).delete(expected);
	}

	@Test
	void givenExistingStudent_whenUpdate_thenEqualOfDaoUpdateMethodCallReturned() {
		int expected = 1;
		Student student = new Student("Kyler", "Donovan", LocalDate.of(1995, 5, 15), "Kiev", "Kyler@Donova",
				"483746578", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		when(studentDao.findByEmail(student.getEmail())).thenReturn(null);
		when(studentDao.findByPhone(student.getPhone())).thenReturn(null);
		when(studentDao.findByAddress(student.getAddress())).thenReturn(null);

		studentService.update(student);

		verify(studentDao, times(expected)).update(student);
	}

	@Test
	void givenNonExistingStudent_whenUpdate_thenDaoUpdateMethodDontCall() {
		Student student = new Student("Graham", "Simon", LocalDate.of(1997, 7, 17), "Everett", "Graham@Simon",
				"293847563", Gender.MALE);
		List<Student> students = new ArrayList<>();
		students.add(student);
		when(studentDao.findByEmail(student.getEmail())).thenReturn(null);

		studentService.update(student);

		verify(studentDao, never()).update(student);
	}

	@Test
	void givenExpectedListOfExistsStudents_whenFindByGroupId_thenRelevantListOfStudentsReturned() {
		Student student = new Student("Clayton", "Braden", LocalDate.of(1999, 6, 14), "Brendan", "Clayton@Braden",
				"3948576238", Gender.MALE);
		List<Student> expected = new ArrayList<>();
		expected.add(student);
		given(studentDao.findByGroupId(student.getId())).willReturn(expected);

		List<Student> actual = studentService.findByGroupId(student.getId());

		assertEquals(expected, actual);
		verify(studentDao, times(1)).findByGroupId(student.getId());
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenCreate_thenEqualOfDaoMethodCallReturned() {
		int expected = 1;
		Student student = new Student("Zander", "Jared", LocalDate.of(2019, 02, 15), "Ivanovo", "Zander@Jared",
				"358769341", Gender.FEMALE);
		List<Student> students = new ArrayList<>();
		when(studentDao.findByEmail(student.getEmail())).thenReturn(null);
		when(studentDao.findByPhone(student.getPhone())).thenReturn(null);
		when(studentDao.findByAddress(student.getAddress())).thenReturn(null);
		when(studentDao.findByGroupId(anyLong())).thenReturn(students);

		studentService.create(student);

		verify(studentDao, times(expected)).create(student);
	}

	@Test
	void givenStudent_whenCreate_thenDaoCreateMethodDontCall() {
		Student student = new Student("Ryker", "Dante", LocalDate.of(2019, 02, 15), "Lane", "Ryker@Dante", "358769341",
				Gender.FEMALE);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	void givenExpectedStudentWithGroupMoreThenThirtyStudentIn_whenCreate_thenDaoCreateMethodDontCall() {
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
	void givenExpectedStudentWithGroupMoreThenThirtyStudentIn_whenUpdate_thenDaoCreateMethodDontCall() {
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

	@Test
	void givenExpectedStudentWithGroupNoMoreThenThirtyStudentIn_whenCreate_thenDaoCreateMethodIsCallExpectedCount() {
		int expected = 1;
		Student student = new Student("Skyler", "Judah", LocalDate.of(2017, 7, 17), "Aden", "Skyler@Judah", "358769341",
				Gender.FEMALE);
		List<Student> students = new ArrayList<>();
		when(studentDao.findByEmail(student.getEmail())).thenReturn(null);
		when(studentDao.findByPhone(student.getPhone())).thenReturn(null);
		when(studentDao.findByAddress(student.getAddress())).thenReturn(null);
		when(studentDao.findByGroupId(student.getGroupId())).thenReturn(students);

		studentService.create(student);

		verify(studentDao, times(expected)).create(student);
	}
}
