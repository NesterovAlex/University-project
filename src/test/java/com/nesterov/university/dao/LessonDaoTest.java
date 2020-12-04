package com.nesterov.university.dao;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Gender;
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
		Subject subject = new Subject(2, "Literature");
		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		Audience audience = new Audience(44, 67);
		audience.setId(3);
		Teacher teacher = new Teacher("Vasya", "Vasin", LocalDate.of(2014, 07, 19), "Vasino", "Vasya@vasyin",
				"2354657657", Gender.MALE);
		teacher.setId(2);
		teacher.setSubjects(subjects);
		LessonTime lessonTime = new LessonTime();
		lessonTime.setId(4);
		lessonTime.setOrderNumber(18);
		lessonTime.setStart(LocalTime.of(18, 30));
		lessonTime.setEnd(LocalTime.of(19, 45));
		lesson = new Lesson();
		ArrayList<Group> groups = new ArrayList<Group>();
		groups.add(new Group(1, "G-45"));
		groups.add(new Group(1, "G-45"));
		groups.add(new Group(2, "Y-12"));
		groups.add(new Group(3, "T-56"));
		groups.add(new Group(3, "T-56"));
		lesson.setId(3);
		lesson.setAudience(audience);
		lesson.setDate(LocalDate.of(2020, 11, 24));
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
		assertEquals(countRowsInTable(jdbcTemplate, "lessons"), lessonDao.findAll().size());
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

	@Test
	void givenNonExistingLessonWIthExpectedDate_whenFindByDate_thenListLessonsWithNullSizeReturned() {
		int expected = 0;

		List<Lesson> actual = lessonDao.findByDate(LocalDate.of(1999, 9, 9));

		assertEquals(expected, actual.size());
	}

	@Test
	void givenExistingLessonWIthExpectedDate_whenFindByDate_thenListLessonsWithExpectedSizeReturned() {
		int expected = 4;

		List<Lesson> actual = lessonDao.findByDate(LocalDate.of(2020, 11, 24));

		assertEquals(expected, actual.size());
	}
}
