package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
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
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {
		studentDao.create(student);
		assertEquals(5, JdbcTestUtils.countRowsInTable(jdbcTemplate, "students"));
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedI() {
		Student student = new Student("Alice", "Nesterova", LocalDate.of(1999, 12, 31), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"));
		student.setGroupId(2);
		student.setId(3);
		student.setFaculty("Biology");
		student.setCourse("Biology");

		studentDao.create(student);

		String actual = jdbcTemplate.queryForObject("SELECT first_name FROM students where id=5", String.class);
		assertEquals("Alice", actual);
	}

	@Test
	void givenDataSetAndIdOfStudent_whenGet_thenExpectedIdOfSudentReturned() {
		Student expected = new Student("Bob", "Sincler", LocalDate.of(2012, 9, 17), "Toronto", "bob@sincler",
				"987654321", Gender.MALE);
		expected.setId(1);
		expected.setFaculty("Biology");
		expected.setCourse("Biology");
		expected.setGroupId(1);
		assertEquals(expected, studentDao.get(1));
	}

	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfStudentInDataBaseReturned() {
		studentDao.delete(3);

		assertEquals(4, JdbcTestUtils.countRowsInTable(jdbcTemplate, "students"));
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedEmailOfStudentReturned() {
		studentDao.update(student);

		assertEquals(5, JdbcTestUtils.countRowsInTable(jdbcTemplate, "students"));
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedNameOfStudentReturned() {
		studentDao.update(student);

		String actual = jdbcTemplate.queryForObject("SELECT first_name FROM students WHERE id=3", String.class);
		assertEquals("Alice", actual);
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedLastOfStudentReturned() {
		studentDao.update(student);

		String actual = jdbcTemplate.queryForObject("SELECT last_name FROM students WHERE id=1", String.class);
		assertEquals("Sincler", actual);
	}

	@Test
	void givenDataSetExpectedStudent_whenGetAll_thenExpectedCountOfStudentReturned() {
		assertEquals(4, studentDao.getAll().size());
	}

	@Test
	void givenDataSetExpectedStudent_whenFindByGroupId_thenExpectedCountOfStudentReturned() {
		assertEquals(1, studentDao.findByGroupId(4).size());
	}
}
