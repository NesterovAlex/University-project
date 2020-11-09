package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Group;
import com.nesterov.university.model.Lesson;
import com.nesterov.university.model.LessonTime;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;


class LessonDaoTest {

	private LessonDao dao;
	private JdbcTemplate template;
	private ApplicationContext context;
	private Lesson lesson;
	
	@BeforeEach
	void setUp() {
		context = new AnnotationConfigApplicationContext(TestConfig.class);
		template = (JdbcTemplate) context.getBean("jdbcTemplate");
		dao = new LessonDao(template);
	}
	
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
		groups.add(new Group(1, "FR-98"));
		groups.add(new Group(2, "YR-38"));
		groups.add(new Group(3, "FU-45"));
		lesson.setId(2);
		lesson.setAudience(audience);
		lesson.setDate(Date.valueOf("2015-03-31"));
		lesson.setGroups(groups);
		lesson.setSubject(subject);
		lesson.setTeacher(teacher);
		lesson.setTime(lessonTime);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedCountOfLessonsReturned() {
		dao.create(lesson);	

		assertEquals(5, JdbcTestUtils.countRowsInTable(template, "lessons"));
	}
	
	@Test
	public void givenExpectedData_whenCreate_thenExpectedGroupsOfLessonInLessons_GroupsDataBaseCreated() {
		dao.create(lesson);	

		assertEquals(7, JdbcTestUtils.countRowsInTable(template, "lessons_groups"));
	}


	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfLessonsReturned() {		
		dao.delete(3);
		
		assertEquals(3, JdbcTestUtils.countRowsInTable(template, "lessons"));
	}
	

	@Test
	void givenDataSet_whenDelete_thenExpectedCountGroupsOfLessonsDeleted() {
		dao.delete(3);
		
		assertEquals(2, JdbcTestUtils.countRowsInTable(template, "lessons_groups"));
	}

	@Test
	void givenDataSetAndExpectedLesson_whenUpdate_thenExpectedCountSubjectsOfLessonUpdated() {
		dao.update(lesson);

		assertEquals(6, JdbcTestUtils.countRowsInTable(template, "lessons_groups"));
	}
	
//	@Test
//	void givenDataSetAndExpectedLesson_whenGet_thenReturnedLessonContainedExpectedTeacher() {
//		assertEquals("Petrov", dao.get(2).getTeacher().getLastName());
//	}
	
	@Test
	void givenDataSetAndExpectedLesson_whenGet_thenExpectedCountOfLessonsReturned() {
		assertEquals(4, dao.getAll().size());
	}
//	
//	@Test
//	void givenDataetAndExpectedLesson_whenGet_thenExpectedGroupCountOfLessonReturned() {
//		assertEquals(1, dao.get(2).getGroups().size());
//	}
}
