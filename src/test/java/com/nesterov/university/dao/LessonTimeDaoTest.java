package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.sql.Time;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotUpdateException;
import com.nesterov.university.model.LessonTime;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class LessonTimeDaoTest {

	@Autowired
	private LessonTimeDao lessonTimeDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void givenExpectedCountRowsInTable_whenCreate_thenEqualCountInTableReturned() throws NotCreateException {
		LessonTime lessonTime = new LessonTime(12, LocalTime.of(8, 40), LocalTime.of(9, 50));
		int expected = countRowsInTable(jdbcTemplate, "lesson_times") + 1;

		lessonTimeDao.create(lessonTime);

		int actual = countRowsInTable(jdbcTemplate, "lesson_times");
		assertEquals(expected, actual);
	}

	@Test
	void givenIdOfExistingLessonTime_whenGet_thenLessonTimeWithGivenIdReturned() throws EntityNotFoundException {
		LessonTime expected = new LessonTime(1, 12, LocalTime.of(13, 30), LocalTime.of(14, 20));
		assertEquals(expected, lessonTimeDao.get(expected.getId()).orElse(null));
	}

	@Test
	void givenExpectedCountRowsInTable_whenDelete_thenEqualCountInTableReturned() throws NotDeleteException {
		int expected = countRowsInTable(jdbcTemplate, "lesson_times") - 1;

		lessonTimeDao.delete(3);

		int actual = countRowsInTable(jdbcTemplate, "lesson_times");
		assertEquals(expected, actual);
	}

	@Test
	void givenIdNotExistinLessonTime_whenDelete_thenNotDeleteExceptionThrown() throws NotDeleteException {
		assertThrows(NotDeleteException.class, () -> lessonTimeDao.delete(399));
	}

	@Test
	void givenOrderNumberOfExistingLessonTime_whenUpdate_thenLessonTimeWithGivenOrderNumberReturned()
			throws NotCreateException {
		LessonTime expected = new LessonTime(4, 12, LocalTime.of(15, 25), LocalTime.of(17, 35));

		lessonTimeDao.update(expected);

		long actual = jdbcTemplate.queryForObject("SELECT order_number FROM lesson_times WHERE id=4", Long.class);
		assertEquals(expected.getOrderNumber(), actual);
	}

	@Test
	void givennotExistingLessonTime_whenUpdate_thenNotUpdateExceptionThrown() throws NotCreateException {
		LessonTime expected = new LessonTime(99, 12, LocalTime.of(15, 25), LocalTime.of(17, 35));

		assertThrows(NotUpdateException.class, () -> lessonTimeDao.update(expected));
	}

	@Test
	void givenStartLessonOfExistingLessonTime_whenUpdate_thenLessonTimeWithGivenLessonStartReturned()
			throws NotCreateException {
		LessonTime expected = new LessonTime(4, 12, LocalTime.of(18, 25), LocalTime.of(16, 50));

		lessonTimeDao.update(expected);

		Time actual = jdbcTemplate.queryForObject("SELECT start_lesson FROM lesson_times WHERE id=4", Time.class);
		assertEquals(expected.getStart(), actual.toLocalTime());
	}

	@Test
	void givenEndLessonOfExistingLessonTime_whenUpdate_henLessonTimeWithGivenLessonEndReturned()
			throws NotCreateException {
		LessonTime expected = new LessonTime(4, 12, LocalTime.of(13, 25), LocalTime.of(17, 45));
		lessonTimeDao.update(expected);

		Time actual = jdbcTemplate.queryForObject("SELECT end_lesson FROM lesson_times WHERE id=4", Time.class);
		assertEquals(expected.getEnd(), actual.toLocalTime());
	}

	@Test
	void givenExpectedCountRowsinTable_whenFindAll_thenEqualCountRowsinTableReturned() throws EntityNotFoundException {
		assertEquals(countRowsInTable(jdbcTemplate, "lesson_times"), lessonTimeDao.findAll().size());
	}
}
