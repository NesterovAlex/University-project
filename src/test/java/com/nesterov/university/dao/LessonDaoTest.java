package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Group;
import com.nesterov.university.model.Lesson;
import com.nesterov.university.model.LessonTime;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class LessonDaoTest {

	@Autowired
	private LessonDao lessonDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private Lesson lesson;

	@BeforeEach
	void initLesson() {
		Subject subject = new Subject();
		subject.setId(23);
		Audience audience = new Audience();
		audience.setId(34);
		Teacher teacher = new Teacher();
		teacher.setId(78);
		LessonTime lessonTime = new LessonTime();
		lessonTime.setId(100);
		lesson = new Lesson();
		ArrayList<Group> groups = new ArrayList<Group>();
		groups.add(new Group(1, "G-45"));
		groups.add(new Group(1, "G-45"));
		groups.add(new Group(2, "Y-12"));
		groups.add(new Group(3, "T-56"));
		groups.add(new Group(3, "T-56"));
		lesson.setId(3);
		lesson.setAudience(audience);
		lesson.setDate(LocalDate.of(2020, 11, 29));
		lesson.setGroups(groups);
		lesson.setSubject(subject);
		lesson.setTeacher(teacher);
		lesson.setTime(lessonTime);
	}

	@Test
	public void givenExpectedCountRowsInTableLessons_whenCreate_thenEqualCountRowsInTableReturned() {
		int expected = countRowsInTable(jdbcTemplate, "lessons") + 1;

		lessonDao.create(lesson);

		int actual = countRowsInTable(jdbcTemplate, "lessons");
		assertEquals(expected, actual);
	}

	@Test
	public void givenExpectedCountRowsInTableLessons_Groups_whenCreate_thenEqualCountRowsInTableReturned() {
		int expected = countRowsInTable(jdbcTemplate, "lessons_groups") + 3;

		lessonDao.create(lesson);

		int actual = countRowsInTable(jdbcTemplate, "lessons_groups");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountRowInTableLessons_Groups_whenUpdate_thenEqualCountRowsInTableReturnedReturned() {
		int expected = countRowsInTable(jdbcTemplate, "lessons_groups") + 1;

		lessonDao.update(lesson);

		int actual = countRowsInTable(jdbcTemplate, "lessons_groups");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountRowsInTableLessons_whenUpdate_thenEqualCountRowsInTableReturned() {
		int expected = countRowsInTable(jdbcTemplate, "lessons");

		lessonDao.update(lesson);

		int actual = countRowsInTable(jdbcTemplate, "lessons");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedTeacherNameOfExistingLesson_whenGet_thenRelevantNameOfTeacherReturned() {
		String expected = "Vasin"; 
		assertEquals(expected, lessonDao.get(4).getTeacher().getLastName());
	}

	@Test
	void givenDataSetAndExpectedLesson_whenFindAll_thenReturnedLessonContainedExpectedTeacher() {
		assertEquals(countRowsInTable(jdbcTemplate, "lessons"), lessonDao.getAll().size());
	}
	
	@Test
	void givenExpectedCountRowsInTableLessons_whenDelete_thenEqualCountRowsInTableReturned() {
		int expected = countRowsInTable(jdbcTemplate, "lessons") - 1;

		lessonDao.delete(1);

		int actual = countRowsInTable(jdbcTemplate, "lessons");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountRowInTableLessons_Groups_whenDelete_thenEqualCountRowsInTableReturned() {
		int expected = countRowsInTable(jdbcTemplate, "lessons_groups") - 1;

		lessonDao.delete(2);

		int actual = countRowsInTable(jdbcTemplate, "lessons_groups");
		assertEquals(expected, actual);
	}
}
