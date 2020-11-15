package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	void givenDataSet_whenDelete_thenExpectedCountOfLessonsReturned() {
		lessonDao.delete(3);

		assertEquals(5, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
	}
	
	@Test
	void givenDataSet_whenDelete_thenExpectedCountGroupsOfLessonsDeleted() {
		lessonDao.delete(3);

		assertEquals(5, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_groups"));
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedCountOfLessonsReturned() {
		lessonDao.create(lesson);

		assertEquals(5, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedGroupsOfLessonInLessons_GroupsDataBaseCreated() {
		lessonDao.create(lesson);

		assertEquals(8, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_groups"));
	}


	@Test
	void givenDataSetAndExpectedLesson_whenUpdate_thenExpectedCountSubjectsOfLessonUpdated() {
		lessonDao.update(lesson);

		assertEquals(10, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons_groups"));
	}
	
	@Test
	void givenDataSetAndExpectedLesson_whenUpdate_thenExpectedCountOfLessonFromDataBaseReturned() {
		lessonDao.update(lesson);

		assertEquals(5, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
	}

	@Test
	void givenDataSetAndExpectedLesson_whenGet_thenReturnedLessonContainedExpectedTeacher() {
		assertEquals("Petrov", lessonDao.get(2).getTeacher().getLastName());
	}
}
