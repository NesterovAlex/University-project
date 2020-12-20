package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Audience;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class AudienceDaoTest {

	@Autowired
	private AudienceDao audienceDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void givenExpectedCountOfRowsInTable_whenDelete_thenEqualCountOfRowsReturned() throws NotExistException {
		int expected = countRowsInTable(jdbcTemplate, "audiences") - 1;

		audienceDao.delete(3);

		int actual = countRowsInTable(jdbcTemplate, "audiences");
		assertEquals(expected, actual);
	}

	@Test
	public void givenExpectedCountOfRowsInTable_whenCreate_thenDifferentCountOfRowsAfterAndBeforeCreatingReturned()
			throws QueryNotExecuteException, NotCreateException {
		int expected = countRowsInTable(jdbcTemplate, "audiences") + 1;

		audienceDao.create(new Audience(14, 87));

		int actual = countRowsInTable(jdbcTemplate, "audiences");
		assertEquals(expected, actual);
	}

	@Test
	void givenIdOfExistingAudience_whenGet_thenAudinceWithGivenIdReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		assertEquals(new Audience(2, 343, 187), audienceDao.get(2));
	}

	@Test
	void givenCapacityOfExistingAudience_whenUpdate_thenRelevantCapacityOfAudenceReturned() throws NotCreateException {
		audienceDao.update(new Audience(3, 999, 1000));

		long actual = jdbcTemplate.queryForObject("SELECT capacity FROM audiences WHERE id=3", Long.class);
		assertEquals(1000, actual);
	}

	@Test
	void givenExpectedRowsFromTable_whenFindAll_thenEqualCountOfAudiencesReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		assertEquals(countRowsInTable(jdbcTemplate, "audiences"), audienceDao.findAll().size());
	}

	@Test
	void givenExistingAudience_whenfindByRoomNumber_thenExpectedAudienceReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		Audience expected = new Audience(2, 343, 187);

		Audience actual = audienceDao.findByRoomNumber(expected.getRoomNumber());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedRoomNumberOfNonExistingAudience_whenfindByRoomNumber_thenNullReturned()
			throws EntityNotFoundException, QueryNotExecuteException {
		Audience actual = null;
		int expected = 99;
		try {
			actual = audienceDao.findByRoomNumber(expected);
		} catch (EntityNotFoundException e) {

		}
		assertNull(actual);
	}
}
