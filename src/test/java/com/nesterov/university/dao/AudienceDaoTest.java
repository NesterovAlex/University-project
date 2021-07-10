package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.nesterov.university.model.Audience;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class AudienceDaoTest {

	@Autowired
	private AudienceDao audienceDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void givenExpectedCountOfRowsInTable_whenDelete_thenEqualCountOfRowsReturned() {
		int expected = countRowsInTable(jdbcTemplate, "audiences") - 1;

		audienceDao.delete(3);

		int actual = countRowsInTable(jdbcTemplate, "audiences");
		assertEquals(expected, actual);
	}

	@Test
	public void givenExpectedCountOfRowsInTable_whenCreate_thenDifferentCountOfRowsAfterAndBeforeCreatingReturned() {
		int expected = countRowsInTable(jdbcTemplate, "audiences") + 1;

		audienceDao.create(new Audience(14, 87));

		int actual = countRowsInTable(jdbcTemplate, "audiences");
		assertEquals(expected, actual);
	}

	@Test
	void givenIdOfExistingAudience_whenGet_thenAudinceWithGivenIdReturned() {
		assertEquals(new Audience(2, 343, 187), audienceDao.get(2).orElse(null));
	}

	@Test
	void givenCapacityOfExistingAudience_whenUpdate_thenRelevantCapacityOfAudenceReturned() {
		audienceDao.update(new Audience(3, 999, 1000));

		long actual = jdbcTemplate.queryForObject("SELECT capacity FROM audiences WHERE id=3", Long.class);
		assertEquals(1000, actual);
	}

	@Test
	void givenExpectedRowsFromTable_whenFindAll_thenEqualCountOfAudiencesReturned() {
		assertEquals(countRowsInTable(jdbcTemplate, "audiences"), audienceDao.findAll().size());
	}

	@Test
	void givenExistingAudience_whenfindByRoomNumber_thenExpectedAudienceReturned() {
		Audience expected = new Audience(2, 343, 187);

		Audience actual = audienceDao.findByRoomNumber(expected.getRoomNumber()).orElse(null);

		assertEquals(expected, actual);
	}

	@Test
	void givenRoomNumberOfNonExistingAudience_whenfindByRoomNumber_thenOptionalEmptyReturn() {
		int expected = 99;
		assertFalse(audienceDao.findByRoomNumber(expected).isPresent());
	}

	@Test
	void givenIdNonExistingAudience_whenGet_thenOptionalEmptyReturned() {
		assertFalse(audienceDao.get(100).isPresent());
	}
}
