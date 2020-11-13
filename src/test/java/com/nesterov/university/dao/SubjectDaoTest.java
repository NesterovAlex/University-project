package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.test.jdbc.JdbcTestUtils;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class SubjectDaoTest {

	@Autowired
	private SubjectDao dao;
	@Autowired
	private JdbcTemplate template;
	private Subject subject;


	@BeforeEach
	void initSubject() {
		List<Teacher> teachers = new ArrayList<Teacher>();
		teachers.add(
				new Teacher("fevaef", "wefqerf", LocalDate.of(2000, 6, 15), "rwefqer", "wfqewrf", "cdwe", Gender.valueOf("FEMALE")));
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
	}

	@Test
	void givenDataSetAndIdOfSubject_whenGet_thenExpectedIdOfSubjectReturned() {
		assertEquals(1, dao.getSubject(1).getId());
	}

	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfSubjectsReturned() {
		dao.delete(3);

		assertEquals(8, JdbcTestUtils.countRowsInTable(template, "subjects"));
	}

	@Test
	void givenDataSetExpectedSubject_whenGetAll_thenExpectedCountOfSubjectsReturned() {
		assertEquals(8, dao.getAll().size());
	}
	
	@Test
	void givenDataSetExpectedSubject_whenGetAllByTeacher_thenExpectedCountOfSubjectsRetured() {
		assertEquals(2, dao.getAllByTeacher(3).size());
	}

}
