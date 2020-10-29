package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.nesterov.university.model.Subject;

class SubjectDaoTest {

	private SubjectDao dao;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/subjects_data.sql").build();
		dao = new SubjectDao(dataSource);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {

		assertEquals(5, dao.create(new Subject("Biology")));
	}

	@Test
	void givenDataSetAndIdOfSubject_whenRead_thenExpectedIdOfSubjectReturned() {

		assertEquals(1, dao.getSubject(1).getId());
	}

	@Test
	void givenDataSetAndIdOfSubject_whenRead_thenExpectedNameOfSubjectReturned() {

		assertEquals("Mathematic", dao.getSubject(1).getName());
	}

	@Test
	void givenDataSet_whenDelete_thenTrueOfDeleteReturned() {

		assertTrue(dao.delete(3));
	}

	@Test
	void givenDataSetExpectedSubject_whenUpdate_thenRelevantParametersOfSubjectUpdated() {
		
		assertEquals(1, dao.update(new Subject(3, "Geometry")));
		
		assertEquals("Geometry", dao.getSubject(3).getName());
	}

}
