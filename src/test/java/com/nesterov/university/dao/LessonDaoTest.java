package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Lesson;
import com.nesterov.university.model.LessonTime;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

class LessonDaoTest {

	private LessonDao dao;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/lessons_data.sql").build();
		dao = new LessonDao(dataSource);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {
		Subject subject = new Subject();
		subject.setId(23);
		Audience audience = new Audience();
		audience.setId(34);
		Teacher teacher = new Teacher();
		teacher.setId(78);
		LessonTime lessonTime = new LessonTime();
		lessonTime.setId(100);
		assertEquals(5, dao.create(new Lesson(subject, audience, LocalDate.now(), lessonTime, teacher)));
	}

	@Test
	void givenDataSetAndIdOfLessonTime_whenRead_thenExpectedIdOfLessonTimeReturned() {

		assertEquals(14, dao.get(1).getId());
	}

	@Test
	void givenDataSetIdOfLessonTime_whenRead_thenExpectedOrderNumberReturned() {

		assertEquals(12, dao.get(1).getSubject().getId());
	}

	@Test
	void givenDataSetIdOfLessonTime_whenRead_thenExpectedCapacityOfLessonTimeReturned() {

		assertEquals(23, dao.get(1).getAudience().getId());
	}

	@Test
	void givenDataSet_whenDeleteLesson_thenTrueOfDeleteReturned() {

		assertTrue(dao.delete(3));
	}

	@Test
	void givenDataSetAndExpectedLesson_whenUpdate_thenRelevantParametersOfLessonUpdated() {
		Subject subject = new Subject();
		subject.setId(23);
		Audience audience = new Audience();
		audience.setId(34);
		Teacher teacher = new Teacher();
		teacher.setId(78);
		LessonTime lessonTime = new LessonTime();
		lessonTime.setId(100);
		
		assertEquals(1, dao.update(new Lesson(2,subject, audience, LocalDate.now(), lessonTime, teacher)));
		
		assertEquals(444, dao.get(3).getTeacher().getId());
	}

}
