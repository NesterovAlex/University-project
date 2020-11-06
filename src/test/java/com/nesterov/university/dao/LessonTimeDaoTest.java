package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Time;
import java.time.LocalTime;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.nesterov.university.model.LessonTime;

class LessonTimeDaoTest {

	private LessonTimeDao dao;
	private JdbcTemplate template;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/lessons_time_data.sql").build();
		template = new JdbcTemplate(dataSource);
		dao = new LessonTimeDao(template);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedCountOfLessonTimesReturned() {
		dao.create(new LessonTime(12, new Time(0),new Time(0)));

		long actual = template.queryForObject("SELECT COUNT(*) FROM lessonTimes", Long.class);
		assertEquals(5, actual);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedStartLessonTimesReturned() {
		dao.create(new LessonTime(12, new Time(0), new Time(0)));

		Time actual = template.queryForObject("SELECT start_lesson FROM lessonTimes where id=5", Time.class);
		assertEquals(LocalTime.of(2, 30), actual.toLocalTime());
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedEndLessonTimesReturned() {
		dao.create(new LessonTime(12, new Time(0), new Time(0)));

		Time actual = template.queryForObject("SELECT end_lesson FROM lessonTimes where id=5", Time.class);
		assertEquals(LocalTime.of(5, 30), actual.toLocalTime());
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedOrderNumberLessonTimesReturned() {
		dao.create(new LessonTime(12, new Time(0), new Time(0)));

		Long actual = template.queryForObject("SELECT order_number FROM lessonTimes where id=5", Long.class);
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

		long actual = template.queryForObject("SELECT COUNT(*) FROM LessonTimes", Long.class);
		assertEquals(3, actual);
	}

	@Test
	void givenDataSetAndExpectedLessonTime_whenUpdate_thenExpectedOrderNumberOfLessonTimeReturned() {
		dao.update(new LessonTime(4, 12, new Time(0), new Time(0)));

		long actual = template.queryForObject("SELECT order_number FROM lessonTimes WHERE id=4", Long.class);
		assertEquals(12, actual);
	}
	
	@Test
	void givenDataSetAndExpectedLessonTime_whenUpdate_thenExpectedStartOfLessonTimeReturned() {
		dao.update(new LessonTime(4, 12, new Time(0), new Time(0)));

		Time actual = template.queryForObject("SELECT start_lesson FROM lessonTimes WHERE id=4", Time.class);
		assertEquals(LocalTime.of(16, 45), actual.toLocalTime());
	}
	
	@Test
	void givenDataSetAndExpectedLessonTime_whenUpdate_thenExpectedEndOfLessonTimeReturned() {
		dao.update(new LessonTime(4, 12, new Time(0), new Time(0)));

		Time actual = template.queryForObject("SELECT end_lesson FROM lessonTimes WHERE id=4", Time.class);
		assertEquals(LocalTime.of(17, 20), actual.toLocalTime());
	}

}
