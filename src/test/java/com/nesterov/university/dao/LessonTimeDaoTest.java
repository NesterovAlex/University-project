package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Time;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.nesterov.university.model.LessonTime;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class LessonTimeDaoTest {

	@Autowired
	private LessonTimeDao lessonTimeDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void givenExpectedData_whenCreate_thenExpectedCountOfLessonTimesReturned() {
		lessonTimeDao.create(new LessonTime(12, LocalTime.of(14, 30), LocalTime.of(13, 45)));

		assertEquals(4, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lesson_times"));
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedOrderNumberLessonTimesReturned() {
		lessonTimeDao.create(new LessonTime(12, LocalTime.of(8, 40), LocalTime.of(9, 50)));

		Long actual = jdbcTemplate.queryForObject("SELECT order_number FROM lesson_times where id=5", Long.class);
		assertEquals(12, actual);
	}

	@Test
	void givenDataSetAndIdOfLessonTime_whenGet_thenExpectedIdOfLessonTimeReturned() {
		assertEquals(new LessonTime(1, 12, LocalTime.of(13, 30), LocalTime.of(14, 20)), lessonTimeDao.get(1));
	}

	@Test
	void givenDataSetIdOfLessonTime_whenGet_thenExpectedOrderNumberReturned() {
		assertEquals(new LessonTime(2, 14, LocalTime.of(14, 45), LocalTime.of(15, 45)), lessonTimeDao.get(2));
	}

	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfLessonTimesReturned() {
		lessonTimeDao.delete(3);

		assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lesson_times"));
	}

	@Test
	void givenDataSetAndExpectedLessonTime_whenUpdate_thenExpectedOrderNumberOfLessonTimeReturned() {
		lessonTimeDao.update(new LessonTime(4, 12, LocalTime.of(15, 25), LocalTime.of(17, 35)));

		long actual = jdbcTemplate.queryForObject("SELECT order_number FROM lesson_times WHERE id=4", Long.class);
		assertEquals(12, actual);
	}

	@Test
	void givenDataSetAndExpectedLessonTime_whenUpdate_thenExpectedStartOfLessonTimeReturned() {
		lessonTimeDao.update(new LessonTime(4, 12, LocalTime.of(18, 25), LocalTime.of(16, 50)));

		Time actual = jdbcTemplate.queryForObject("SELECT start_lesson FROM lesson_times WHERE id=4", Time.class);
		assertEquals(LocalTime.of(18, 25), actual.toLocalTime());
	}

	@Test
	void givenDataSetAndExpectedLessonTime_whenUpdate_thenExpectedEndOfLessonTimeReturned() {
		lessonTimeDao.update(new LessonTime(4, 12, LocalTime.of(13, 25), LocalTime.of(17, 45)));

		Time actual = jdbcTemplate.queryForObject("SELECT end_lesson FROM lesson_times WHERE id=4", Time.class);
		assertEquals(LocalTime.of(17, 45), actual.toLocalTime());
	}
}
