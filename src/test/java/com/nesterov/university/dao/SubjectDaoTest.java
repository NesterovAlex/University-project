package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.nesterov.university.model.Subject;

class SubjectDaoTest {

	private SubjectDao dao;
	private JdbcTemplate template;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/subjects_data.sql").build();
		template = new JdbcTemplate(dataSource);
		dao = new SubjectDao(template);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedNameOfSubjectReturned() {
		dao.create(new Subject("Biology"));

		String actual = template.queryForObject("SELECT name FROM subjects WHERE id=5", String.class);
		assertEquals("Biology", actual);
	}

	@Test
	public void givenExpectedData_whenCreate_thenCountOfSubjectsReturned() {
		dao.create(new Subject("Biology"));

		long actual = template.queryForObject("SELECT COUNT(*) FROM subjects", Long.class);
		assertEquals(5, actual);
	}

	@Test
	void givenDataSetAndIdOfSubject_whenGet_thenExpectedNameOfSubjectReturned() {
		String expected = dao.getSubject(1).getName();

		String actual = template.queryForObject("SELECT name FROM subjects WHERE id=1", String.class);
		assertEquals(expected, actual);
	}

	@Test
	void givenDataSetAndIdOfSubject_whenGet_thenExpectedIdOfSubjectReturned() {
		assertEquals(1, dao.getSubject(1).getId());
	}

	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfSubjectsReturned() {
		dao.delete(3);

		long actual = template.queryForObject("SELECT COUNT(*) FROM subjects", Long.class);
		assertEquals(3, actual);
	}

	@Test
	void givenDataSetExpectedSubject_whenUpdate_thenRelevantParametersOfSubjectUpdated() {
		dao.update(new Subject(3, "Geometry"));

		String actual = template.queryForObject("SELECT name FROM subjects where id=3", String.class);
		assertEquals("Geometry", actual);
	}

}
