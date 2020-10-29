package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.nesterov.university.model.LessonTime;

class LessonTimeDaoTest {

	private LessonTimeDao dao;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/lessons_time_data.sql").build();
		dao = new LessonTimeDao(dataSource);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {

		assertEquals(5, dao.create(new LessonTime(12, LocalTime.now(), LocalTime.now())));
	}

	@Test
	void givenDataSetAndIdOfLessonTime_whenRead_thenExpectedIdOfLessonTimeReturned() {

		assertEquals(1, dao.get(1).getId());
	}

	@Test
	void givenDataSetIdOfLessonTime_whenRead_thenExpectedOrderNumberReturned() {

		assertEquals(12, dao.get(1).getOrderNumber());
	}

	@Test
	void givenDataSetIdOfLessonTime_whenRead_thenExpectedCapacityOfLessonTimeReturned() {

		assertEquals(12, dao.get(1).getOrderNumber());
	}

	@Test
	void givenDataSet_whenDeleteLessonTime_thenTrueOfDeleteReturned() {

		assertTrue(dao.delete(3));
	}

	@Test
	void givenDataSetAndExpectedLessonTime_whenUpdate_thenRelevantParametersOfLessonTimeUpdated() {
		
		assertEquals(1, dao.update(new LessonTime(4, 12, LocalTime.now(), LocalTime.now())));
		
		assertEquals(16, dao.get(3).getOrderNumber());
	}

}
