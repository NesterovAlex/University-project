package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

class TeacherDaoTest {

	private TeacherDao dao;
	private JdbcTemplate template;
	private ApplicationContext context;

	@BeforeEach
	void setUp() {
		context = new AnnotationConfigApplicationContext(TestConfig.class);
		template = (JdbcTemplate) context.getBean("jdbcTemplate");
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

		assertEquals(9, JdbcTestUtils.countRowsInTable(template, "teachers"));
	}

	@Test
	public void givenExpectedData_whenCreate_thenRelavantSubjectsOfTeacherInTeachers_SubjectsDataBaseCreated() {
		List<Subject> subjects = new ArrayList<Subject>();
		subjects.add(new Subject("Java"));
		Teacher teacher = new Teacher("Alice", "Nesterova", new Date(0), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setSubjects(subjects);

		dao.create(teacher);

		assertEquals(7, JdbcTestUtils.countRowsInTable(template, "teachers_subjects"));
	}

	@Test
	void givenDataSetAndIdOfTeacher_whenGet_thenExpectedIdOfTeacherReturned() {
		assertEquals(1, dao.get(1).getId());
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
	void givenDataSetExpectedStudent_whenUpdate_thenExpectedEmailOfTeacherReturned() {
		Teacher teacher = new Teacher("Alice", "Nesterova", new Date(0), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setId(3);
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(45, "Filosophy"));
		subjects.add(new Subject(46, "Paleontology"));
		teacher.setSubjects(subjects);
		dao.update(teacher);

		String actual = template.queryForObject("SELECT email FROM teachers WHERE id=3", String.class);
		assertEquals("alice@nesterova.com", actual);
	}

	@Test
	void givenTestData_whenUpdate_thenRelevantSubjectsOfTeacherFromTableTeachers_SubjectsDeleted() {
		Teacher teacher = new Teacher("Alice", "Nesterova", new Date(0), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setId(3);
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(45, "Filosophy"));
		teacher.setSubjects(subjects);
		dao.update(teacher);
		assertEquals(3, JdbcTestUtils.countRowsInTable(template, "teachers_subjects"));
	}

	@Test
	void givenTestData_whenUpdate_thenExpectedCountSubjectsOfTeacherInsertToTeachers_Subjects() {
		JdbcTestUtils.deleteFromTables(template, "teachers_subjects");
		Teacher teacher = new Teacher("Alice", "Nesterova", new Date(0), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setId(6);
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(45, "Filosophy"));
		subjects.add(new Subject(46, "Paleontology"));
		subjects.add(new Subject(47, "Geography"));
		teacher.setSubjects(subjects);

		dao.update(teacher);

		assertEquals(3, JdbcTestUtils.countRowsInTable(template, "teachers_subjects"));
	}
	
	@Test
	void givenTestData_whenDelete_thenExpectedCountSubjectsOfTeacherDeletedFromTeachers_Subjects() {
		dao.delete(3);

		assertEquals(2, JdbcTestUtils.countRowsInTable(template, "teachers_subjects"));
	}
	
	@Test
	void givenTestData_whenDelete_thenExpectedCountOfTeacherDeleted() {
		dao.delete(3);

		assertEquals(7, JdbcTestUtils.countRowsInTable(template, "teachers"));
	}
	
	@Test
	void givenTestData_whenGetAll_thenExpectedTeachersReturned() {
		assertEquals(8, dao.getAll().size());
	}
	
	@Test
	void givenTestData_whenGetAllBySubject_thenExpectedTeachersReturned() {
		assertEquals(3, dao.getAllBySubject(2).size());
	}
	
	@Test
	void givenTestData_whenGetAllBySubject_thenExpectedSubjectCountOfTeacherReturned() {
		assertEquals(4, dao.get(3).getSubjects().size());
	}
}
