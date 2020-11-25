package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.nesterov.university.dao.TestConfig;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class SubjectServiceTest {

	@Autowired
	private SubjectService subjectService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Subject subject;
	private List<Teacher> teachers;
	private Teacher teacher;

	@BeforeEach
	void setUp() throws Exception {
		subject = new Subject("Literature");
		subject.setId(9);
		teacher = new Teacher("Bob", "Sincler", LocalDate.of(2012, 9, 17), "Toronto", "bob@sincler", "987654321",
				Gender.MALE);
		teacher.setId(1);
		teachers = Arrays.asList(teacher);
		subject.setTeachers(teachers);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void givenExpectedIdOfSubject_whenCreateSubject_thenEqualIdOfCreatedSubjectReturned() {
		long expected = 9;

		Subject actual = subjectService.createSubject(subject);

		assertEquals(expected, actual.getId());
	}

	@Test
	void givenExpectedSubject_whenGetSubject_thenEqualSubjectReturned() {
		Teacher firstTeacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 7, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		firstTeacher.setId(2);
		firstTeacher.setSubjects(asList(new Subject("Mathematic")));
		Teacher secondTeacher = new Teacher("Petr", "Petrov", LocalDate.of(2011, 5, 14), "Petrovka", "petr@petrov",
				"55r2346254", Gender.MALE);
		secondTeacher.setSubjects(asList(new Subject("Mathematic")));
		Subject expected = new Subject(1, "Mathematic");
		expected.setTeachers(Arrays.asList(firstTeacher, secondTeacher));

		Subject actual = subjectService.getSubject(expected);

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedIdOfSubject_whenDeleteSubject_thenEqualIdOfDeletedSubjectReturned() {
		long expected = 9;

		long actual = subjectService.deleteSubject(subject);

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedNameOfExistingSubject_whenUpdateSubject_thenRelevantNameOfSubjectReturned() {
		Subject expected = new Subject(8, "Literature");
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 7, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(asList(new Subject("Mathematic")));
		expected.setTeachers(asList(teacher));

		subjectService.updateSubject(expected);

		String actual = jdbcTemplate.queryForObject("SELECT name FROM subjects WHERE id = ?",
				new Object[] { expected.getId() }, String.class);
		assertEquals(expected.getName(), actual);
	}

	@Test
	void givenCountRowInTable_whenfindAll_thenEqualCountOfSubjectsReturned() {
		int expected = countRowsInTable(jdbcTemplate, "subjects");

		int actual = subjectService.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountSubjectsOfExistingTeacher_whenfindAllByTeacher_thenEqualCountOfSubjectsReturned() {
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 7, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		int expected = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM subjects LEFT JOIN teachers_subjects ON subjects.id = teachers_subjects.subject_id WHERE teacher_id = ?",
				new Object[] { teacher.getId() }, Integer.class);

		int actual = subjectService.findByTeacherId(teacher.getId()).size();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountTeachersOfExistingSubject_whenAddTeacher_thenEqualCountTeachersFromDataBaseReturned() {
		Subject expected = new Subject(8, "Literature");
		Teacher added = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 7, 19), "Vasino", "Vasya@vasyin", "2354657657",
				Gender.MALE);
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher);
		added.setId(2);
		added.setSubjects(asList(new Subject("Mathematic")));
		expected.setTeachers(teachers);

		subjectService.addTeacher(expected, added);

		int actual = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM teachers_subjects WHERE subject_id = ?",
				new Object[] { expected.getId() }, Integer.class);
		assertEquals(expected.getTeachers().size(), actual);
	}
	
	@Test
	void givenExpectedCountTeachersOfExistingSubject_whenRemoveTeacher_thenExpectedCountTeachersFromDataBaseReturned() {
		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 7, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(asList(new Subject("Mathematic")));
		Teacher deleted = new Teacher("Petr", "Petrov", LocalDate.of(2011, 5, 14), "Petrovka", "petr@petrov",
				"55r2346254", Gender.MALE);
		deleted.setId(2);
		deleted.setSubjects(asList(new Subject("Mathematic")));
		Subject subject = new Subject(1, "Mathematic");
		teachers.add(teacher);
		teachers.add(deleted);
		subject.setTeachers(teachers);
		int expected = countRowsInTable(jdbcTemplate, "teachers_subjects") - 1;
		
		subjectService.removeTeacher(subject, deleted);
		
		int actual = countRowsInTable(jdbcTemplate, "teachers_subjects");
		assertEquals(expected, actual);
	}
}
