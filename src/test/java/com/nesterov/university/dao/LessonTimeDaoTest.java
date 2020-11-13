package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Time;
import java.time.LocalTime;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.nesterov.university.model.LessonTime;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class LessonTimeDaoTest {

	@Autowired
	private LessonTimeDao dao;
	@Autowired
	private JdbcTemplate template;

	@Test
	public void givenExpectedData_whenCreate_thenExpectedCountOfLessonTimesReturned() {
		dao.create(new LessonTime(12, LocalTime.of(14, 30), LocalTime.of(13, 45)));

		assertEquals(4, JdbcTestUtils.countRowsInTable(template, "lesson_times"));
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedOrderNumberLessonTimesReturned() {
		dao.create(new LessonTime(12, LocalTime.of(8, 40), LocalTime.of(9, 50)));

		Long actual = template.queryForObject("SELECT order_number FROM lesson_times where id=5", Long.class);
		assertEquals(12, actual);
	}

	@Test
	void givenDataSetAndIdOfLessonTime_whenGet_thenExpectedIdOfLessonTimeReturned() {
		assertEquals(1, dao.get(1).getId());
	}

	@Test
	void givenDataSetIdOfLessonTime_whenGet_thenExpectedOrderNumberReturned() {
		assertEquals(12, dao.get(1).getOrderNumber());
	}

	@Test
	void givenDataSetIdOfLessonTime_whenGet_thenExpectedStartOfLessonTimeReturned() {
		assertEquals(LocalTime.of(13, 30), dao.get(1).getStart());
	}

	@Test
	void givenDataSetIdOfLessonTime_whenGet_thenExpectedEndOfLessonTimeReturned() {
		assertEquals(LocalTime.of(14, 45), dao.get(2).getStart());
	}

	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfLessonTimesReturned() {
		dao.delete(3);

		assertEquals(3, JdbcTestUtils.countRowsInTable(template, "lesson_times"));
	}

	@Test
	void givenDataSetAndExpectedLessonTime_whenUpdate_thenExpectedOrderNumberOfLessonTimeReturned() {
		dao.update(new LessonTime(4, 12, LocalTime.of(15, 25), LocalTime.of(17, 35)));

		long actual = template.queryForObject("SELECT order_number FROM lesson_times WHERE id=4", Long.class);
		assertEquals(12, actual);
	}
	
	@Test
	void givenDataSetAndExpectedLessonTime_whenUpdate_thenExpectedStartOfLessonTimeReturned() {
		dao.update(new LessonTime(4, 12, LocalTime.of(18, 25), LocalTime.of(16, 50)));

		Time actual = template.queryForObject("SELECT start_lesson FROM lesson_times WHERE id=4", Time.class);
		assertEquals(LocalTime.of(18, 25), actual.toLocalTime());
	}
	
	@Test
	void givenDataSetAndExpectedLessonTime_whenUpdate_thenExpectedEndOfLessonTimeReturned() {
		dao.update(new LessonTime(4, 12, LocalTime.of(13, 25), LocalTime.of(17, 45)));

		Time actual = template.queryForObject("SELECT end_lesson FROM lesson_times WHERE id=4", Time.class);
		assertEquals(LocalTime.of(17, 45), actual.toLocalTime());
	}
}
