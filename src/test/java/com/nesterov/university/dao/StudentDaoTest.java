package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Student;

class StudentDaoTest {

	private StudentDao dao;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/students_data.sql").build();
		dao = new StudentDao(dataSource);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {

		assertEquals(5, dao.create(new Student("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"))));
	}

	@Test
	void givenDataSetAndIdOfStudent_whenRead_thenExpectedIdOfSudentReturned() {

		assertEquals(1, dao.get(1).getId());
	}

	@Test
	void givenDataSetAndIdOfStudent_whenRead_thenExpectedFirstNameOfStudentReturned() {

		assertEquals("Bob", dao.get(1).getFirstName());
	}

	@Test
	void givenDataSetAndIdOfStudent_whenRead_thenExpectedLastNameStudentReturned() {

		assertEquals("Sincler", dao.get(1).getLastName());
	}

	@Test
	void givenDataSet_whenDeleteAudience_thenTrueOfDeleteReturned() {

		assertTrue(dao.delete(3));
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenRelevantParametersOfStudentUpdated() {
		Student student = new Student("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		student.setId(3);
		assertEquals(1, dao.update(student));

		assertEquals("alice@nesterova.com", dao.get(3).getEmail());
	}
}
