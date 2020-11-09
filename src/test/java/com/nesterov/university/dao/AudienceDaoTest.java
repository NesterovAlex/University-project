package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.nesterov.university.model.Audience;

@SpringJUnitConfig(TestConfig.class)
class AudienceDaoTest {

	private AudienceDao dao;
	private JdbcTemplate template;
	private ApplicationContext context;

	@BeforeEach
	void setUp() {
		context = new AnnotationConfigApplicationContext(TestConfig.class);
		template = (JdbcTemplate) context.getBean("jdbcTemplate");
		dao = new AudienceDao(template);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnedExpectedCountCountRowsInTable() {
		dao.create(new Audience(14, 87));

		assertEquals(5, JdbcTestUtils.countRowsInTable(template, "audiences"));
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnedCurentCountRowsInTableWhereIdEqualFive() {
		dao.create(new Audience(14, 87));

		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(template, "audiences", "id = 5"));
	}

	@Test
	void givenDataSetAndIdOfAudience_whenGet_thenExpectedIdOfAudienceReturned() {
		assertEquals(1, dao.get(1).getId());
	}

	@Test
	void givenDataSetIdOfAudience_whenGet_thenExpectedRoomNumberOfAudienceReturned() {
		assertEquals(14, dao.get(1).getRoomNumber());
	}

	@Test
	void givenDataSetIdOfAudience_whenGet_thenExpectedCapacityOfAudienceReturned() {
		assertEquals(87, dao.get(1).getCapacity());
	}

	@Test
	void givenDataSet_whenDeleteAudience_thenExpectedCountOfAudienceReturned() {
		dao.delete(3);

		assertEquals(3, JdbcTestUtils.countRowsInTable(template, "audiences"));
	}

	@Test
	void givenDataSetExpectedAudience_whenUpdate_thenRelevantRoomNumberOfAudenceUpdated() {
		dao.update(new Audience(3, 999, 1000));

		long actual = template.queryForObject("SELECT room_number FROM audiences WHERE id=3", Long.class);
		assertEquals(999, actual);
	}

	@Test
	void givenDataSetExpectedAudience_whenUpdate_thenRelevantCapacityOfAudenceUpdated() {
		dao.update(new Audience(3, 999, 1000));

		long actual = template.queryForObject("SELECT capacity FROM audiences WHERE id=3", Long.class);
		assertEquals(1000, actual);
	}
	
	@Test
	void givenDataSetExpectedAudience_whenGetAll_thenExpectedCountOfAudiencesReturned() {
		assertEquals(4, dao.getAll().size());
	}
}
