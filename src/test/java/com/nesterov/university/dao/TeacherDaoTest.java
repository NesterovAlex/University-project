package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Teacher;

class TeacherDaoTest {

	private TeacherDao dao;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/teachers_data.sql").build();
		dao = new TeacherDao(dataSource);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {

		assertEquals(5, dao.create(new Teacher("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"))));
	}

	@Test
	void givenDataSetAndIdOfTeacher_whenRead_thenExpectedIdOfTeacherReturned() {

		assertEquals(1, dao.getTeacher(1).getId());
	}

	@Test
	void givenDataSetAndIdOfTeacher_whenRead_thenExpectedFirstNameOfTeacherReturned() {

		assertEquals("Bob", dao.getTeacher(1).getFirstName());
	}

	@Test
	void givenDataSetAndIdOfTeacher_whenRead_thenExpectedLastNameTeacherReturned() {

		assertEquals("Sincler", dao.getTeacher(1).getLastName());
	}

	@Test
	void givenDataSet_whenDeleteAudience_thenTrueOfDeleteReturned() {

		assertTrue(dao.delete(3));
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenRelevantParametersOfStudentUpdated() {
		Teacher teacher = new Teacher("Alice", "Nesterova", LocalDate.now(), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setId(3);
		assertEquals(1, dao.update(teacher));

		assertEquals("alice@nesterova.com", dao.getTeacher(3).getEmail());
	}

}
