package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Student;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	@Mock
	private StudentDao studentDao;

	@InjectMocks
	private StudentService studentService;

	@Spy
	List<Student> students = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		students.add(new Student("Ivan", "Ivanov", LocalDate.of(1999, 6, 14), "Kiev", "Ivan@Ivanov", "123456789",
				Gender.MALE));
	}

	@Test
	void givenExpectedListOfExistsStudents_whenGetAll_thenRelevantListOfStudentsReturned() {
		List<Student> expected = new ArrayList<>();
		expected.add(students.get(0));
		given(studentDao.getAll()).willReturn(expected);

		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedStudent_whenGet_thenEqualStudentReturned() {
		final Student expected = students.get(0);
		given(studentDao.get(anyLong())).willReturn(expected);

		final Student actual = studentService.get(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		doNothing().when(studentDao).delete(anyLong());

		studentService.delete(anyLong());

		verify(studentDao, times(1)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoUpdateMethodCall_whenUpdate_thenEqualOfDaoUpdateMethodCallReturned() {
		int expected = 2;
		Student student = students.get(0);
		doNothing().when(studentDao).update(any(Student.class));

		studentService.update(student);
		studentService.update(student);

		verify(studentDao, times(expected)).update(student);
	}

	@Test
	void givenExpectedListOfExistsStudents_whenFindByGroupId_thenRelevantListOfStudentsReturned() {
		List<Student> expected = new ArrayList<>();
		expected.add(students.get(0));
		given(studentDao.findByGroupId(anyLong())).willReturn(expected);

		List<Student> actual = studentService.findByGroupId(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenCreate_EqualOfDaoMethodCallReturned() {
		Student student = students.get(0);
		doThrow(new EmptyResultDataAccessException(1)).when(studentDao).get(student.getId());

		studentService.create(students.get(0));

		verify(studentDao, times(1)).create(student);
	}
}
