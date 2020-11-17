package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Group;
import com.nesterov.university.model.Lesson;
import com.nesterov.university.model.LessonTime;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
@Sql(scripts = { "/schema.sql", "classpath:test_data.sql" })
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
		groups.add(new Group(1, "YR-38"));
		groups.add(new Group(1, "YR-38"));
		groups.add(new Group(2, "YR-38"));
		groups.add(new Group(3, "FU-45"));
		groups.add(new Group(3, "FU-45"));
		lesson.setId(2);
		lesson.setAudience(audience);
		lesson.setDate(LocalDate.of(2020, 11, 29));
		lesson.setGroups(groups);
		lesson.setSubject(subject);
		lesson.setTeacher(teacher);
		lesson.setTime(lessonTime);
	}

	@Test
	void givenDataSet_whenDelete_thenDifferentCountOfRowsBeforeAndAfterDeletingReturned() {
		int countRowsBeforeDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");

		lessonDao.delete(3);

		int countRowsafterDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");
		assertFalse(countRowsBeforeDelete == countRowsafterDelete);
	}

	@Test
	void givenDataSet_whenDelete_thenDifferentLessonsBeforeAndAfterDeletingReturned() {
		int countRowsBeforeDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_groups");
		lessonDao.delete(3);

		int countRowsAfterDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_groups");
		assertFalse(countRowsBeforeDelete == countRowsAfterDelete);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedCountOfLessonsReturned() {
		int countRowsBeforeCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");

		lessonDao.create(lesson);

		int countRowsAfterCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");
		assertFalse(countRowsBeforeCreate == countRowsAfterCreate);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedGroupsOfLessonInLessons_GroupsDataBaseCreated() {
		int countRowsBeforeCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_groups");

		lessonDao.create(lesson);

		int countRowsAfterCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_groups");
		assertFalse(countRowsBeforeCreate == countRowsAfterCreate);
	}

	@Test
	void givenDataSetAndExpectedLesson_whenUpdate_thenDifferentCountGroupsOfLessonsBeforeAndAfterUpdatingReturned() {
		int countRowsBeforeUpdate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_groups");

		lessonDao.update(lesson);

		int countRowsAfterUpdate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_groups");
		assertFalse(countRowsBeforeUpdate == countRowsAfterUpdate);
	}

	@Test
	void givenDataSetAndExpectedLesson_whenUpdate_thenEqualCountOfLessonsFromDataBaseBeforeAndAfterUpdatingReturned() {
		int countRowsBeforeUpdate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_groups");

		lessonDao.update(lesson);

		int countRowsAfterUpdate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");
		assertTrue(countRowsBeforeUpdate == countRowsAfterUpdate);
	}

	@Test
	void givenDataSetAndExpectedLesson_whenGet_thenReturnedLessonContainedExpectedTeacher() {
		assertEquals("Petrov", lessonDao.get(2).getTeacher().getLastName());
	}
}
