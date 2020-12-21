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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class SubjectDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubjectDaoTest.class);

	@Autowired
	private SubjectDao subjectDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Subject subject;
	private Teacher teacher;

	@BeforeEach
	void initSubject() {
		List<Teacher> teachers = new ArrayList<Teacher>();
		teacher = new Teacher("Petr", "Petrov", LocalDate.of(2011, 5, 14), "Petrovka", "petr@petrov", "55r2346254",
				Gender.MALE);
		teacher.setId(3);
		teachers.add(teacher);
		subject = new Subject(1, "Biology");
		subject.setTeachers(teachers);
	}

	@Test
	void givenExpectedCountRowsInTable_whenUpdate_thenEqualCountRowsReturned()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		int expected = countRowsInTable(jdbcTemplate, "teachers_subjects");

		subjectDao.update(subject);

		int actual = countRowsInTable(jdbcTemplate, "teachers_subjects");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountRowsInTable_whenFindAll_thenExpectedCountOfSubjectsReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		assertEquals(countRowsInTable(jdbcTemplate, "subjects"), subjectDao.findAll().size());
	}

	@Test
	public void givenExpectedCountRowsInTable_whenCreate_thenEqualCountRowsReturned() throws NotCreateException {
		int expected = countRowsInTable(jdbcTemplate, "subjects") + 1;

		subjectDao.create(subject);

		int actual = countRowsInTable(jdbcTemplate, "subjects");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedIdOfExistingSubject_whenGet_thenRelevantSubjectWithExpectedIdReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		int expected = 8;
		assertEquals(new Subject(expected, "Physic"), subjectDao.get(expected));
	}

	@Test
	void givenExpectedTeachersOfExistingSubject_whenGet_thenRelevantTeachersOfSubjectReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 7, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		Teacher otherTacher = new Teacher("Petr", "Petrov", LocalDate.of(2011, 5, 14), "Petrovka", "petr@petrov",
				"55r2346254", Gender.MALE);
		otherTacher.setId(3);
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher);
		teachers.add(otherTacher);

		assertTrue(containsAll(teachers, subjectDao.get(subject.getId()).getTeachers()));
	}

	@Test
	void givenExpectedIdOfExistingSubject_whenGet_thenEqualIdOfSubjectReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		Subject expected = new Subject(7, "Geometry");

		assertEquals(expected, subjectDao.get(expected.getId()));
	}

	@Test
	void givenExpectedRowsInTable_whenDelete_thenEqualCountRowsReturned() throws NotExistException {
		int expected = countRowsInTable(jdbcTemplate, "subjects") - 1;

		subjectDao.delete(2);

		int actual = countRowsInTable(jdbcTemplate, "subjects");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountSubjectsOfTeacher_whenGetAllByTeacher_thenExpectedCountOfSubjectsRetured()
			throws QueryNotExecuteException, EntityNotFoundException {
		int expected = 3;
		assertEquals(expected, subjectDao.findByTeacherId(teacher.getId()).size());
	}

	@Test
	void givenExpectedCountRowsInTable_whenUpdate_thenEqualCountRowsInTableReturned()
			throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		int expected = countRowsInTable(jdbcTemplate, "subjects");

		subjectDao.update(subject);

		int actual = countRowsInTable(jdbcTemplate, "subjects");
		assertEquals(expected, actual);
	}

	@Test
	void givenExistingSubject_whenFindByName_thenExpectedSubjectReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		Subject expected = new Subject(9, "Design");

		Subject actual = subjectDao.findByName(expected.getName());

		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistingSubject_whenFindByName_thenNullReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		Subject expected = new Subject(10, "Health");
		Subject actual = null;
		try {
			actual = subjectDao.findByName(expected.getName());
		} catch (EntityNotFoundException e) {
			LOGGER.error("Not found subject by name = '{}'", expected.getName(), e);
		}
		assertNull(actual);
	}
}
