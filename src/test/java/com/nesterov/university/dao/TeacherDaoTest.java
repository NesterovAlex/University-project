package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Order;
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
class TeacherDaoTest {

	@Autowired
	private TeacherDao dao;
	@Autowired
	private JdbcTemplate template;

	@Test
	void givenTestData_whenDelete_thenExpectedCountSubjectsOfTeacherDeletedFromTeachers_Subjects() {
		dao.delete(3);

		assertEquals(5, JdbcTestUtils.countRowsInTable(template, "teachers_subjects"));
	}
	
	@Test
	void givenTestData_whenDelete_thenExpectedCountOfTeacherDeleted() {
		dao.delete(3);

		assertEquals(9, JdbcTestUtils.countRowsInTable(template, "teachers"));
	}
		
	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {
		List<Subject> subjects = new ArrayList<Subject>();
		subjects.add(new Subject(8, "Java"));
		Teacher teacher = new Teacher("Alice", "Nesterova", LocalDate.of(2015, 2, 12), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setSubjects(subjects);

		dao.create(teacher);

		assertEquals(10, JdbcTestUtils.countRowsInTable(template, "teachers"));
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedRowsInTeachers_SubjectsDataBaseReturned() {
		List<Subject> subjects = new ArrayList<Subject>();
		subjects.add(new Subject(9,"Java"));
		Teacher teacher = new Teacher("Alice", "Nesterova", LocalDate.of(2004, 5, 3), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setSubjects(subjects);

		dao.create(teacher);

		assertEquals(6, JdbcTestUtils.countRowsInTable(template, "teachers_subjects"));
	}

	@Test
	@Order(3)
	void givenDataSetAndIdOfTeacher_whenGet_thenExpectedIdOfTeacherReturned() {
		assertEquals(1, dao.get(1).getId());
	}

	@Test
	@Order(4)
	void givenDataSetAndIdOfTeacher_whenGet_thenExpectedFirstNameOfTeacherReturned() {
		assertEquals("Bob", dao.get(1).getFirstName());
	}

	@Test
	void givenDataSetAndIdOfTeacher_whenGet_thenExpectedLastNameTeacherReturned() {
		assertEquals("Sincler", dao.get(1).getLastName());
	}

	@Test
	void givenDataSetExpectedTeacher_whenUpdate_thenExpectedSubjectsInTeachers_SubjectsReturned() {
		Teacher teacher = new Teacher("Alice", "Nesterova", LocalDate.of(1995, 9, 9), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setId(2);
		List<Subject> subjects = new ArrayList<>();
		subjects.add(new Subject(2, "Filosophy"));
		subjects.add(new Subject(2, "Filosophy"));
		subjects.add(new Subject(46, "Paleontology"));
		subjects.add(new Subject(47, "Paleontology"));
		teacher.setSubjects(subjects);

		dao.update(teacher);

		assertEquals(7, JdbcTestUtils.countRowsInTable(template, "teachers_subjects"));
	}

	@Test
	void givenTestData_whenUpdate_thenExpectedTeachersFromDataBaseReturned() {
		Teacher teacher = new Teacher("Alice", "Nesterova", LocalDate.of(1998, 7, 20), "Kiev", "alice@nesterova.com", "123456789",
				Gender.valueOf("FEMALE"));
		teacher.setId(3);
		List<Subject> subjects = new ArrayList<>();
		teacher.setSubjects(subjects);
		dao.update(teacher);
		assertEquals(8, JdbcTestUtils.countRowsInTable(template, "teachers"));
	}

	@Test
	void givenTestData_whenGetAll_thenExpectedTeachersReturned() {
		assertEquals(9, dao.getAll().size());
	}
	
	@Test
	void givenTestData_whenGetAllBySubject_thenExpectedTeachersReturned() {
		assertEquals(2, dao.getAllBySubject(2).size());
	}
	
	@Test
	void givenTestData_whenGetAllBySubject_thenExpectedSubjectCountOfTeacherReturned() {
		assertEquals(1, dao.get(1).getSubjects().size());
	}
}
