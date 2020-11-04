package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Student;

class StudentDaoTest {

	private StudentDao dao;
	private JdbcTemplate template;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/students_data.sql").build();
		template = new JdbcTemplate(dataSource);
		dao = new StudentDao(template);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {
		dao.create(new Student("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE")));

		long actual = template.queryForObject("SELECT COUNT(*) FROM students", Long.class);
		assertEquals(5, actual);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedI() {
		dao.create(new Student("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE")));

		String actual = template.queryForObject("SELECT first_name FROM students where id=5", String.class);
		assertEquals("Alice", actual);
	}

	@Test
	void givenDataSetAndIdOfStudent_whenGet_thenExpectedIdOfSudentReturned() {
		assertEquals(1, dao.get(1).getId());
	}

	@Test
	void givenDataSetAndIdOfStudent_whenRead_thenExpectedFirstNameOfStudentReturned() {
		assertEquals("Bob", dao.get(1).getFirstName());
	}

	@Test
	void givenDataSetAndIdOfStudent_whenGet_thenExpectedLastNameStudentReturned() {
		assertEquals("Sincler", dao.get(1).getLastName());
	}

	@Test
	void givenDataSet_whenDeleteAudience_thenTrueOfDeleteReturned() {
		dao.delete(3);
		
		long actual = template.queryForObject("SELECT COUNT(*) FROM students", Long.class);
		assertEquals(3, actual);
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedEmailOfStudentReturned() {
		Student student = new Student("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		student.setId(3);

		dao.update(student);

		String actual = template.queryForObject("SELECT email FROM students WHERE id=3", String.class);
		assertEquals("alice@nesterova.com", actual);
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedNameOfStudentReturned() {
		Student student = new Student("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		student.setId(3);

		dao.update(student);

		String actual = template.queryForObject("SELECT first_name FROM students WHERE id=3", String.class);
		assertEquals("Alice", actual);
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedLastOfStudentReturned() {
		Student student = new Student("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		student.setId(3);

		dao.update(student);

		String actual = template.queryForObject("SELECT last_name FROM students WHERE id=3", String.class);
		assertEquals("Nesterova", actual);
	}
}
