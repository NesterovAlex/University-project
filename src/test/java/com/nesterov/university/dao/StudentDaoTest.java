package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Student;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class StudentDaoTest {

	@Autowired
	private StudentDao dao;
	@Autowired
	private JdbcTemplate template;
	private Student student;

	@BeforeEach
	void initStudent() {
		student = new Student("Alice", "Nesterova", LocalDate.of(2020, 11, 14), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		student.setGroupId(2);
		student.setId(3);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {
		dao.create(student);
		
		assertEquals(5, JdbcTestUtils.countRowsInTable(template, "students"));
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedI() {
		dao.create(new Student("Alice", "Nesterova", LocalDate.of(1999, 12, 31), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE")));

		String actual = template.queryForObject("SELECT first_name FROM students where id=5", String.class);
		assertEquals("Alice", actual);
	}

	@Test
	void givenDataSetAndIdOfStudent_whenGet_thenExpectedIdOfSudentReturned() {
		assertEquals(1, dao.get(1).getId());
	}

	@Test
	void givenDataSetAndIdOfStudent_whenGet_thenExpectedFirstNameOfStudentReturned() {
		assertEquals("Bob", dao.get(1).getFirstName());
	}

	@Test
	void givenDataSetAndIdOfStudent_whenGet_thenExpectedLastNameStudentReturned() {
		assertEquals("Sincler", dao.get(1).getLastName());
	}

	@Test
	void givenDataSet_whenDeleteAudience_thenExpectedCountOfStudentInDataBaseReturned() {
		dao.delete(3);

		assertEquals(5, JdbcTestUtils.countRowsInTable(template, "students"));
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedEmailOfStudentReturned() {
		dao.update(student);

		String actual = template.queryForObject("SELECT email FROM students WHERE id=3", String.class);
		assertEquals("alice@nesterova.com", actual);
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedNameOfStudentReturned() {
		dao.update(student);

		String actual = template.queryForObject("SELECT first_name FROM students WHERE id=3", String.class);
		assertEquals("Alice", actual);
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedLastOfStudentReturned() {
		dao.update(student);

		String actual = template.queryForObject("SELECT last_name FROM students WHERE id=3", String.class);
		assertEquals("Nesterova", actual);
	}
	
	@Test
	void givenDataSetExpectedStudent_whenGetAll_thenExpectedCountOfStudentReturned() {
		assertEquals(4, dao.getAll().size());
	}
	
	@Test
	void givenDataSetExpectedStudent_whenGetAllByGroup_thenExpectedCountOfStudentReturned() {
		assertEquals(1, dao.getAllByGroup(4).size());
	}
}
