package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Student;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class StudentDaoTest {

	@Autowired
	private StudentDao studentDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Student student;

	@BeforeEach
	void initStudent() {
		student = new Student("Alice", "Nesterova", LocalDate.of(2020, 11, 14), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"));
		student.setGroupId(2);
		student.setId(3);
		student.setFaculty("Biology");
		student.setCourse("Biology");
	}

	@Test
	public void givenExpectedCountRowsInTable_whenCreate_thenEqualCountRowsFromTableReturned() {
		int expected = countRowsInTable(jdbcTemplate, "students") + 1;

		studentDao.create(student);

		int actual = countRowsInTable(jdbcTemplate, "students");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedOfExistingStudent_whenGet_thenEqualStudentReturned() {
		Student expected = new Student("Ivanka", "Ivanova", LocalDate.of(2019, 02, 15), "Ivanovo", "ivanka@ivanova",
				"358769341", Gender.FEMALE);
		expected.setId(4);
		expected.setFaculty("Biology");
		expected.setCourse("Biology");
		expected.setGroupId(4);

		assertEquals(expected, studentDao.get(expected.getId()));
	}

	@Test
	void givenExpectedCountRowsInTable_whenDelete_thenEqualCountRowsFromTableReturned() {
		int expected = countRowsInTable(jdbcTemplate, "students") - 1;

		studentDao.delete(1);

		int actual = countRowsInTable(jdbcTemplate, "students");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountRowsInTable_whenUpdate_thenEqualCountRowsFromTableReturned() {
		int expected = countRowsInTable(jdbcTemplate, "students");

		studentDao.update(student);

		int actual = countRowsInTable(jdbcTemplate, "students");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedNameOfExistingStudent_whenUpdate_thenStudentWithGivenNameReturned() {
		studentDao.update(student);

		String actual = jdbcTemplate.queryForObject("SELECT first_name FROM students WHERE id=3", String.class);
		assertEquals("Alice", actual);
	}

	@Test
	void givenDataSetExpectedStudent_whenFindAll_thenExpectedCountOfStudentReturned() {
		assertEquals(countRowsInTable(jdbcTemplate, "students"), studentDao.findAll().size());
	}

	@Test
	void givenExpectedIdOfExistingGroup_whenFindByGroupId_thenExpectedCountOfStudentReturned() {
		int expected = 4;

		assertEquals(1, studentDao.findByGroupId(expected).size());
	}

	@Test
	void givenExpectedEmailOfExistingStudent_whenFindByEmail_thenRelevantListOfStudentsReturned() {
		String email = "Michael@Fisher";
		Student student = new Student("Michael", "Fisher", LocalDate.of(2006, 02, 13), "Salem", "Michael@Fisher",
				"3947852847", Gender.MALE);
		student.setCourse("Literature");
		student.setFaculty("Literature");
		student.setId(5);
		student.setGroupId(5);
		List<Student> expected = new ArrayList<>();
		expected.add(student);

		List<Student> actual = studentDao.findByEmail(email);

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedPhoneOfExistingStudent_whenFindByPhone_thenRelevantListOfStudentsReturned() {
		String phone = "358769341";
		Student student = new Student("Ivanka", "Ivanova", LocalDate.of(2019, 02, 15), "Ivanovo", "ivanka@ivanova",
				"358769341", Gender.FEMALE);
		student.setCourse("Biology");
		student.setFaculty("Biology");
		student.setId(4);
		student.setGroupId(4);
		List<Student> expected = new ArrayList<>();
		expected.add(student);

		List<Student> actual = studentDao.findByPhone(phone);

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedPhoneOfExistingStudent_whenFindByAddress_thenRelevantListOfStudentsReturned() {
		String address = "Garlem";
		Student student = new Student("Hank", "Moody", LocalDate.of(2003, 06, 14), "Garlem", "Hank@Moody", "6439037583",
				Gender.MALE);
		student.setCourse("History");
		student.setFaculty("History");
		student.setId(6);
		student.setGroupId(6);
		List<Student> expected = new ArrayList<>();
		expected.add(student);

		List<Student> actual = studentDao.findByAddress(address);

		assertEquals(expected, actual);
	}
}
