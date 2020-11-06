package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Lesson;
import com.nesterov.university.model.LessonTime;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

class LessonDaoTest {

	private LessonDao dao;
	private JdbcTemplate template;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/lessons_data.sql").build();
		template = new JdbcTemplate(dataSource);
		dao = new LessonDao(template);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedCountOfLessonsReturned() {
		Subject subject = new Subject();
		subject.setId(23);
		Audience audience = new Audience();
		audience.setId(34);
		Teacher teacher = new Teacher();
		teacher.setId(78);
		LessonTime lessonTime = new LessonTime();
		lessonTime.setId(100);
		
		dao.create(new Lesson(subject, audience, new Date(0), lessonTime, teacher));	

		long actual = template.queryForObject("SELECT COUNT(*) FROM lessons", Long.class);
		assertEquals(5, actual);
	}
	
	@Test
	public void givenExpectedData_whenCreate_thenExpectedAudienceIdReturned() {
		Subject subject = new Subject();
		subject.setId(23);
		Audience audience = new Audience();
		audience.setId(34);
		Teacher teacher = new Teacher();
		teacher.setId(78);
		LessonTime lessonTime = new LessonTime();
		lessonTime.setId(100);
		
		dao.create(new Lesson(subject, audience, new Date(0), lessonTime, teacher));	

		long actual = template.queryForObject("SELECT audience_id FROM lessons WHERE id=5", Long.class);
		assertEquals(34, actual);
	}


	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfLessonsReturned() {		
		dao.delete(3);
		
		long actual = template.queryForObject("SELECT COUNT(*) FROM lessons", Long.class);
		assertEquals(3, actual);
	}

	@Test
	void givenDataSetAndExpectedLesson_whenUpdate_thenExpectedSubjectIdOfLessonUpdated() {
		Subject subject = new Subject();
		subject.setId(23);
		Audience audience = new Audience();
		audience.setId(34);
		Teacher teacher = new Teacher();
		teacher.setId(78);
		LessonTime lessonTime = new LessonTime();
		lessonTime.setId(100);

		dao.update(new Lesson(2,subject, audience, new Date(0), lessonTime, teacher));

		long actual = template.queryForObject("SELECT subject_id FROM lessons WHERE id=2", Long.class);
		assertEquals(23, actual);
	}
	
	@Test
	void givenDataSetAndExpectedLesson_whenUpdate_thenExpectedTeacherIdOfLessonUpdated() {
		Subject subject = new Subject();
		subject.setId(23);
		Audience audience = new Audience();
		audience.setId(34);
		Teacher teacher = new Teacher();
		teacher.setId(78);
		LessonTime lessonTime = new LessonTime();
		lessonTime.setId(100);

		dao.update(new Lesson(2,subject, audience, new Date(0), lessonTime, teacher));

		long actual = template.queryForObject("SELECT teacher_id FROM lessons WHERE id=2", Long.class);
		assertEquals(78, actual);
	}

}
