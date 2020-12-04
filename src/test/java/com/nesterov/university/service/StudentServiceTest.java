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
		Student student = new Student("Ivan", "Ivanov", LocalDate.of(1999, 6, 14), "Kiev", "Ivan@Ivanov", "123456789",
				Gender.MALE);
		List<Student> expected = new ArrayList<>();
		expected.add(student);
		given(studentDao.findAll()).willReturn(expected);

		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
		verify(studentDao, times(1)).findAll();
	}

	@Test
	void givenExpectedStudent_whenGet_thenEqualStudentReturned() {
		Student expected = new Student("Ivan", "Ivanov", LocalDate.of(1999, 6, 14), "Kiev", "Ivan@Ivanov", "123456789",
				Gender.MALE);
		given(studentDao.get(anyLong())).willReturn(expected);

		Student actual = studentService.get(anyLong());

		assertEquals(expected, actual);
		verify(studentDao, times(1)).get(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		int expected = 1;

		studentService.delete(anyLong());

		verify(studentDao, times(expected)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoUpdateMethodCall_whenUpdate_thenEqualOfDaoUpdateMethodCallReturned() {
		int expected = 2;
		Student student = new Student("Ivan", "Ivanov", LocalDate.of(1999, 6, 14), "Kiev", "Ivan@Ivanov", "123456789",
				Gender.MALE);
		when(studentDao.findByEmail(any(String.class))).thenReturn(null);
		when(studentDao.findByPhone(any(String.class))).thenReturn(null);
		when(studentDao.findByAddress(any(String.class))).thenReturn(null);

		studentService.update(student);
		studentService.update(student);

		verify(studentDao, times(expected)).update(student);
	}

	@Test
	void givenExpectedListOfExistsStudents_whenFindByGroupId_thenRelevantListOfStudentsReturned() {
		Student student = new Student("Ivan", "Ivanov", LocalDate.of(1999, 6, 14), "Kiev", "Ivan@Ivanov", "123456789",
				Gender.MALE);
		List<Student> expected = new ArrayList<>();
		expected.add(student);
		given(studentDao.findByGroupId(anyLong())).willReturn(expected);

		List<Student> actual = studentService.findByGroupId(anyLong());

		assertEquals(expected, actual);
		verify(studentDao, times(1)).findByGroupId(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenCreate_EqualOfDaoMethodCallReturned() {
		int expected = 1;
		Student student = new Student("Ivanka", "Ivanova", LocalDate.of(2019, 02, 15), "Ivanovo", "ivanka@ivanova",
				"358769341", Gender.FEMALE);
		when(studentDao.findByEmail(any(String.class))).thenReturn(null);
		when(studentDao.findByPhone(any(String.class))).thenReturn(null);
		when(studentDao.findByAddress(any(String.class))).thenReturn(null);

		studentService.create(student);

		verify(studentDao, times(expected)).create(student);
	}

	@Test
	void givenStudent_whenCreate_thenDaoCreateMethodDontCall() {
		Student student = new Student("Ivanka", "Ivanova", LocalDate.of(2019, 02, 15), "Ivanovo", "ivanka@ivanova",
				"358769341", Gender.FEMALE);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}
}
