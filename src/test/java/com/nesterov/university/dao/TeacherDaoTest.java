package com.nesterov.university.dao;

import static org.apache.commons.collections4.CollectionUtils.containsAll;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

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
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class TeacherDaoTest {

	@Autowired
	private TeacherDao teacherDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Teacher teacher;

	@BeforeEach
	void Init() {
		List<Subject> subjects = new ArrayList<Subject>();
		teacher = new Teacher("Alice", "Nesterova", LocalDate.of(2015, 2, 12), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"));
		subjects.add(new Subject(8, "Java"));
		subjects.add(new Subject(3, "Geometry"));
		teacher.setSubjects(subjects);
		teacher.setId(3);
	}

	@Test
	public void givenExpectedCountRowsInTableTeachers_whenCreate_thenEqualCountRowsReturned()
			throws NotCreateException {
		List<Subject> subjects = new ArrayList<Subject>();
		Teacher teacher = new Teacher("Alice", "Nesterova", LocalDate.of(2015, 2, 12), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"));
		subjects.add(new Subject(8, "Java"));
		subjects.add(new Subject(3, "Java"));
		teacher.setSubjects(subjects);
		int expected = countRowsInTable(jdbcTemplate, "teachers") + 1;

		teacherDao.create(teacher);

		int actual = countRowsInTable(jdbcTemplate, "teachers");
		assertEquals(expected, actual);
	}

	@Test
	public void givenExpectedCountRowsInTableTeachers_Subjects_whenCreate_thenEqualCountRowsReturned()
			throws NotCreateException {
		int expected = countRowsInTable(jdbcTemplate, "teachers_subjects") + 2;

		teacherDao.create(teacher);

		int actual = countRowsInTable(jdbcTemplate, "teachers_subjects");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountRowsInTableTeachers_Subjects_whenDelete_thenEqualCountRowsReturned()
			throws NotExistException {
		int expected = countRowsInTable(jdbcTemplate, "teachers_subjects") - 2;

		teacherDao.delete(teacher.getId());

		int actual = countRowsInTable(jdbcTemplate, "teachers_subjects");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountRowsInTableTeachers_whenDelete_thenEqualCountRowsReturned() throws NotExistException {
		int expected = countRowsInTable(jdbcTemplate, "teachers") - 1;

		teacherDao.delete(4);

		int actual = countRowsInTable(jdbcTemplate, "teachers");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedIdOfExistingTeacher_whenGet_thenRelevantTeacherReturned() throws EntityNotFoundException {
		Teacher expected = new Teacher("Petr", "Petrov", LocalDate.of(2011, 5, 14), "Petrovka", "petr@petrov",
				"55r2346254", Gender.MALE);
		expected.setId(7);

		assertEquals(expected, teacherDao.get(expected.getId()));
	}

	@Test
	void givenExpectedIdOfExistingTeacher_whenGet_thenRelevantListOfSubjectsReturned() throws EntityNotFoundException {
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(3, "Geometry"));
		subjects.add(new Subject(2, "Geography"));

		assertTrue(containsAll(subjects, teacherDao.get(1).getSubjects()));
	}

	@Test
	void givenExpectedCountRowsInTableTeachers_Subjects_whenUpdate_thenEqualCountRowsReturned()
			throws NotCreateException, EntityNotFoundException {
		Teacher updated = new Teacher("Alice", "Nesterova", LocalDate.of(1995, 9, 9), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"));
		updated.setId(2);
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(2, "Geography"));
		subjects.add(new Subject(4, "Physic"));
		subjects.add(new Subject(4, "Physic"));
		subjects.add(new Subject(3, "Geometry"));
		subjects.add(new Subject(1, "Mathematic"));
		subjects.add(new Subject(5, "Mathematic"));
		updated.setSubjects(subjects);
		int expected = countRowsInTable(jdbcTemplate, "teachers_subjects") + 3;

		teacherDao.update(updated);

		int actual = countRowsInTable(jdbcTemplate, "teachers_subjects");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountRowsInTableTeachers_whenUpdate_thenEqualCountRowsReturned()
			throws NotCreateException, EntityNotFoundException {
		Teacher updated = new Teacher("Alice", "Nesterova", LocalDate.of(1995, 9, 9), "Kiev", "alice@nesterova.com",
				"123456789", Gender.valueOf("FEMALE"));
		updated.setId(2);
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(2, "Geography"));
		subjects.add(new Subject(3, "Geometry"));
		subjects.add(new Subject(1, "Mathematic"));
		updated.setSubjects(subjects);
		int expected = countRowsInTable(jdbcTemplate, "teachers");

		teacherDao.update(updated);

		int actual = countRowsInTable(jdbcTemplate, "teachers");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountRowsInTableTeachers_whenFindAll_thenExpectedRowsTeachersReturned()
			throws EntityNotFoundException {
		assertEquals(countRowsInTable(jdbcTemplate, "teachers"), teacherDao.findAll().size());
	}

	@Test
	void givenSubjectId_whenGetAllBySubject_thenExpectedCountOfTeachersReturned() throws EntityNotFoundException {
		Subject actual = new Subject(2, "Geography");
		int expected = 3;

		assertEquals(expected, teacherDao.findBySubjectId(actual.getId()).size());
	}

	@Test
	void givenTeacherEmail_whenFindByEmail_thenExpectedTeacherReturned() throws EntityNotFoundException {
		Teacher expected = new Teacher("Michael", "Fisher", LocalDate.of(2006, 02, 13), "Salem", "Michael@Fisher",
				"3947852847", Gender.MALE);
		expected.setId(11);

		Teacher actual = teacherDao.findByEmail(expected.getEmail());

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherPhone_whenFindByPhone_thenExpectedTeacherReturned() throws EntityNotFoundException {
		Teacher expected = new Teacher("John", "Conor", LocalDate.of(2000, 04, 13), "New York", "John@Connor",
				"3847562903", Gender.MALE);
		expected.setId(9);

		Teacher actual = teacherDao.findByPhone(expected.getPhone());

		assertEquals(expected, actual);
	}

	@Test
	void givenTeacherAddress_whenFindByAddress_thenExpectedTeacherReturned() throws EntityNotFoundException {
		Teacher expected = new Teacher("Hank", "Moody", LocalDate.of(2003, 06, 14), "Garlem", "Hank@Moody",
				"6439037583", Gender.MALE);
		expected.setId(10);

		Teacher actual = teacherDao.findByAddress(expected.getAddress());

		assertEquals(expected, actual);
	}
}
