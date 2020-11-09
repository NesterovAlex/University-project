package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

class SubjectDaoTest {

	private SubjectDao dao;
	private JdbcTemplate template;
	private ApplicationContext context;
	private Subject subject;

	@BeforeEach
	void setUp() {
		context = new AnnotationConfigApplicationContext(TestConfig.class);
		template = (JdbcTemplate) context.getBean("jdbcTemplate");
		dao = new SubjectDao(template);
	}

	@BeforeEach
	void initSubject() {
		List<Teacher> teachers = new ArrayList<Teacher>();
		teachers.add(
				new Teacher("fevaef", "wefqerf", new Date(0), "rwefqer", "wfqewrf", "cdwe", Gender.valueOf("FEMALE")));
		subject = new Subject("Biology");
		subject.setTeachers(teachers);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedCountOfSubjectsFromDatabaseReturned() {
		dao.create(subject);

		assertEquals(9, JdbcTestUtils.countRowsInTable(template, "subjects"));
	}

	@Test
	void givenDataSetAndIdOfSubject_whenGet_thenExpectedNameOfSubjectReturned() {
		String expected = dao.getSubject(1).getName();

		String actual = template.queryForObject("SELECT name FROM subjects WHERE id=1", String.class);
		assertEquals(expected, actual);
		System.out.println(dao.getSubject(1).getTeachers().get(0).getAddress());
	}

	@Test
	void givenDataSetAndIdOfSubject_whenGet_thenExpectedIdOfSubjectReturned() {
		assertEquals(1, dao.getSubject(1).getId());
	}

	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfSubjectsReturned() {
		dao.delete(3);

		assertEquals(7, JdbcTestUtils.countRowsInTable(template, "subjects"));
	}

	@Test
	void givenDataSetExpectedSubject_whenUpdate_thenRelevantParametersOfSubjectUpdated() {
		dao.update(new Subject(3, "Geometry"));

		String actual = template.queryForObject("SELECT name FROM subjects where id=3", String.class);
		assertEquals("Geometry", actual);
	}

	@Test
	void givenDataSetExpectedSubject_whenGetAll_thenExpectedCountOfSubjectsReturned() {
		assertEquals(8, dao.getAll().size());
	}
	
	@Test
	void givenDataSetExpectedSubject_whenGetAllByTeacher_thenExpectedCountOfSubjectsRetured() {
		assertEquals(4, dao.getAllByTeacher(3).size());
	}

}
