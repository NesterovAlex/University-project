package com.nesterov.university.dao;

import static org.apache.commons.collections4.CollectionUtils.containsAll;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
@Sql(scripts = { "/schema.sql", "classpath:test_data.sql" })
class TeacherDaoTest {

	@Autowired
	private TeacherDao teacherDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Teacher updated;

	@BeforeEach
	void Init() {
		List<Subject> subjects = new ArrayList<Subject>();
		updated = new Teacher("Alice", "Nesterova", LocalDate.of(2015, 2, 12), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"));
		subjects.add(new Subject(8, "Java"));
		subjects.add(new Subject(3, "Java"));
		updated.setSubjects(subjects);
	}

	@Test
	public void givenDataSet_whenCreate_thenDifferentCountOfRowsBeforAndAfterCreatingReturned() {
		int countRowsBeforeCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");

		teacherDao.create(updated);

		int countRowsAfterCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
		assertFalse(countRowsBeforeCreate == countRowsAfterCreate);
	}

	@Test
	public void givenTestDataSet_whenCreate_thenDifferentCountSubjectsOfTeacherBeforAndAfterCreatingReturned() {
		int countRowsBeforeCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers_subjects");

		teacherDao.create(updated);

		int countRowsAfterCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers_subjects");
		assertFalse(countRowsBeforeCreate == countRowsAfterCreate);
	}

	@Test
	void givenTestData_whenDelete_thenDifferentCountSubjectsOfTeacherBeforeAndAfterDeletingReturned() {
		int countRowsBeforeDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers_subjects");

		teacherDao.delete(3);

		int countRowsAfterDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers_subjects");
		assertFalse(countRowsBeforeDelete == countRowsAfterDelete);
	}

	@Test
	void givenTestData_whenDelete_thenExpectedCountOfTeacherBeforeAndAfterDeleingReturned() {
		int countRowsBeforeDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");

		teacherDao.delete(3);

		int countRowsAfterDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
		assertFalse(countRowsBeforeDelete == countRowsAfterDelete);
	}

	@Test
	void givenDataSet_whenGet_thenExpectedTeacherReturned() {
		Teacher expected = new Teacher("Petr", "Petrov", LocalDate.of(2011, 5, 14), "Petrovka", "petr@petrov",
				"55r2346254", Gender.MALE);
		expected.setId(7);

		assertEquals(expected, teacherDao.get(expected.getId()));
	}

	@Test
	void givenDataSetAndExpectedListSubjectsOfTeacher_whenGet_thenListOfSubjectsReturned() {
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(1, "Mathematic"));
		subjects.add(new Subject(2, "Geography"));

		assertTrue(containsAll(subjects, teacherDao.get(1).getSubjects()));
	}

	@Test
	void givenDataSetExpectedTeacher_whenUpdate_thenDifferentCountSubjctsOfTeacherBeforeAndAfterUpdatingReturned() {
		Teacher updated = new Teacher("Alice", "Nesterova", LocalDate.of(1995, 9, 9), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"));
		updated.setId(2);
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(2, "Filosophy"));
		subjects.add(new Subject(4, "Paleontology"));
		subjects.add(new Subject(4, "Paleontology"));
		subjects.add(new Subject(3, "Paleontology"));
		subjects.add(new Subject(1, "Pleontology"));
		subjects.add(new Subject(5, "Paleontology"));
		updated.setSubjects(subjects);
		int countRowsBeforeUpdate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers_subjects");

		teacherDao.update(updated);

		int countRowsAfterUpdate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers_subjects");
		assertFalse(countRowsBeforeUpdate == countRowsAfterUpdate);
	}

	@Test
	void givenTestData_whenUpdate_thenEqualCountOfTeacherBeforeAndAfterUpdatingReturned() {
		Teacher updated = new Teacher("Alice", "Nesterova", LocalDate.of(1995, 9, 9), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"));
		updated.setId(2);
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(2, "Filosophy"));
		subjects.add(new Subject(3, "Paleontology"));
		subjects.add(new Subject(1, "Pleontology"));
		updated.setSubjects(subjects);
		int countRowsBeforeUpdate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");

		teacherDao.update(updated);

		int countRowsAfterUpdate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
		assertTrue(countRowsBeforeUpdate == countRowsAfterUpdate);
	}

	@Test
	void givenTestData_whenGetAll_thenExpectedTeachersReturned() {
		assertEquals(8, teacherDao.getAll().size());
	}
	
	@Test
	void givenTestData_whenGetAllBySubject_thenExpectedTeachersReturned() {
		assertEquals(3, teacherDao.findBySubjectId(2).size());
	}
	
	@Test
	void givenTestData_whenGetAllBySubject_thenExpectedSubjectCountOfTeacherReturned() {
		assertEquals(1, teacherDao.get(1).getSubjects().size());
	}
}
