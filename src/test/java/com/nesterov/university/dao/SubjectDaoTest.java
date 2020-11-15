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
	private SubjectDao subjectDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Subject subject;

	@BeforeEach
	void initSubject() {
		List<Teacher> teachers = new ArrayList<Teacher>();
		Teacher teacher = new Teacher("fevaef", "wefqerf", LocalDate.of(2000, 6, 15), "rwefqer", "wfqewrf", "cdwe",
				Gender.valueOf("FEMALE"));
		teacher.setId(4);
		teachers.add(teacher);
		subject = new Subject(4, "Biology");
		subject.setTeachers(teachers);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedCountOfSubjectsFromDatabaseReturned() {
		subjectDao.create(subject);

		assertEquals(9, JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects"));
	}

	@Test
	void givenDataSetAndIdOfSubject_whenGet_thenExpectedNameOfSubjectReturned() {
		assertEquals(new Subject(1, "Mathematic"), subjectDao.get(1));
	}

	@Test
	void givenDataSetExpectedListTeachersOfSubject_whenGet_thenExpectedTeachersListOfSubjectReturne() {
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 7, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		Teacher otherTacher = new Teacher("Petr", "Petrov", LocalDate.of(2011, 5, 14), "Petrovka", "petr@petrov",
				"55r2346254", Gender.MALE);
		otherTacher.setId(3);
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher);
		teachers.add(otherTacher);
		assertTrue(containsAll(teachers, subjectDao.get(1).getTeachers()));
	}

	@Test
	void givenDataSetAndIdOfSubject_whenGet_thenExpectedIdOfSubjectReturned() {
		assertEquals(new Subject(7, "Geometry"), subjectDao.get(7));
	}

	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfSubjectsReturned() {
		subjectDao.delete(3);

		assertEquals(8, JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects"));
	}

	@Test
	void givenDataSetExpectedSubject_whenGetAll_thenExpectedCountOfSubjectsReturned() {
		assertEquals(8, subjectDao.getAll().size());
	}

	@Test
	void givenDataSetExpectedSubject_whenGetAllByTeacher_thenExpectedCountOfSubjectsRetured() {
		assertEquals(2, subjectDao.findByTeacherId(3).size());
	}

	@Test
	void givenDataSetExpectedSubject_whenUpdate_thenExpectedCountOfSubjectsFromDataBaseReturned() {
		subjectDao.update(subject);

		assertEquals(8, JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects"));
	}

}
