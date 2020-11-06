package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

class TeacherDaoTest {

	private TeacherDao dao;
	private JdbcTemplate template;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/teachers_data.sql").build();
		template = new JdbcTemplate(dataSource);
		dao = new TeacherDao(template);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {
		List<Subject> subjects = new ArrayList<Subject>();
				subjects.add(new Subject("Java"));
			Teacher teacher = new Teacher("Alice", "Nesterova", new Date(0), "Kiev", "alice@nesterova.com", "123456789",
						Gender.valueOf("FEMALE"));
			teacher.setSubjects(subjects);
		dao.create(teacher);

		long actual = template.queryForObject("SELECT COUNT(*) FROM teachers", Long.class);
		assertEquals(5, actual);
	}

//	@Test
//	public void givenExpectedData_whenCreate_thenReturnExpectedPhone() {
//		dao.create(new Teacher("Alice", "Nesterova", new Date(0), "Kiev", "alice@nesterova.com", "123456789",
//				Gender.valueOf("FEMALE")));
//
//		String actual = template.queryForObject("SELECT phone FROM teachers WHERE id=5", String.class);
//		assertEquals("123456789", actual);
//	}

	@Test
	void givenDataSetAndIdOfTeacher_whenGet_thenExpectedIdOfTeacherReturned() {
		assertEquals(1, dao.get(1).getId());
//		System.out.println(dao.get(3).getSubjects().get(0).getName());
	}

	@Test
	void givenDataSetAndIdOfTeacher_whenGet_thenExpectedFirstNameOfTeacherReturned() {
		assertEquals("Bob", dao.get(1).getFirstName());
	}

	@Test
	void givenDataSetAndIdOfTeacher_whenGet_thenExpectedLastNameTeacherReturned() {
		assertEquals("Sincler", dao.get(1).getLastName());
	}

	@Test
	void givenDataSet_whenDeleteAudience_thenExpectedCountOfTeachersReturned() {
		dao.delete(3);
		
		long actual = template.queryForObject("SELECT COUNT(*) FROM teachers", Long.class);
		assertEquals(3, actual);
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedLastNamesOfTeacherReturned() {
		Teacher teacher = new Teacher("Alice", "Nesterova", new Date(0), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setId(3);

		dao.update(teacher);

		String actual = template.queryForObject("SELECT last_name FROM teachers WHERE id=3", String.class);
		assertEquals("Nesterova", actual);
	}

	@Test
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedEmailOfTeacherReturned() {
		Teacher teacher = new Teacher("Alice", "Nesterova", new Date(0), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setId(3);

		dao.update(teacher);

		String actual = template.queryForObject("SELECT email FROM teachers WHERE id=3", String.class);
		assertEquals("alice@nesterova.com", actual);
	}
	
	@Test
	void test() {
		String f = dao.getAllBySubject(1).get(0).getFirstName();
		System.out.println(f);
	}

}
