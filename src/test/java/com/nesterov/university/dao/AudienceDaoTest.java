package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.nesterov.university.model.Audience;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
@Sql(scripts = { "/schema.sql", "classpath:test_data.sql" })
class AudienceDaoTest {

	@Autowired
	private AudienceDao audienceDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void givenDataSet_whenDeleteAudience_thenOtherCountOfRowsReturned() {
		int beforeDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");

		audienceDao.delete(3);

		int afterDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");
		assertFalse(beforeDelete == afterDelete);
	}

	@Test
	public void givenExpectedData_whenCreate_thenDifferentCountOfRowsAfterAndBeforeCreatingreturned() {
		int beforeCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");

		audienceDao.create(new Audience(14, 87));

		int afterCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");
		assertFalse(beforeCreate == afterCreate);
	}

	@Test
	void givenDataSetAndIdOfAudience_whenGet_thenExpectedIdOfAudienceReturned() {
		assertEquals(1, audienceDao.get(1).getId());
	}

	@Test
	void givenDataSetIdOfAudience_whenGet_thenExpectedRoomNumberOfAudienceReturned() {
		assertEquals(new Audience(2, 343, 187), audienceDao.get(2));
	}

	@Test
	void givenDataSetExpectedAudience_whenUpdate_thenRelevantRoomNumberOfAudenceUpdated() {
		audienceDao.update(new Audience(3, 999, 1000));

		long actual = jdbcTemplate.queryForObject("SELECT room_number FROM audiences WHERE id=2", Long.class);
		assertEquals(343, actual);
	}

	@Test
	void givenDataSetExpectedAudience_whenUpdate_thenRelevantCapacityOfAudenceUpdated() {
		audienceDao.update(new Audience(3, 999, 1000));

		long actual = jdbcTemplate.queryForObject("SELECT capacity FROM audiences WHERE id=3", Long.class);
		assertEquals(1000, actual);
	}

	@Test
	void givenDataSetExpectedAudience_whenGetAll_thenExpectedCountOfAudiencesReturned() {
		assertEquals(4, audienceDao.getAll().size());
	}
}
