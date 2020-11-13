package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.nesterov.university.model.Audience;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class AudienceDaoTest {

	@Autowired
	private AudienceDao audienceDao;
	@Autowired
	private JdbcTemplate template;

	@Test
	void givenDataSet_whenDeleteAudience_thenExpectedCountOfAudienceReturned() {
		audienceDao.delete(3);

		assertEquals(3, JdbcTestUtils.countRowsInTable(template, "audiences"));
	}
	
	@Test
	public void givenExpectedData_whenCreate_thenReturnedExpectedCountCountRowsInTable() {
		audienceDao.create(new Audience(14, 87));

		assertEquals(4, JdbcTestUtils.countRowsInTable(template, "audiences"));	
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnedCurentCountRowsInTableWhereIdEqualFive() {
		audienceDao.create(new Audience(14, 87));
		
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(template, "audiences", "id = 5"));	
		JdbcTestUtils.deleteFromTableWhere(template, "audiences", "id=4");
	}

	@Test
	void givenDataSetAndIdOfAudience_whenGet_thenExpectedIdOfAudienceReturned() {
		assertEquals(1, audienceDao.get(1).getId());
	}

	@Test
	void givenDataSetIdOfAudience_whenGet_thenExpectedRoomNumberOfAudienceReturned() {
		assertEquals(14, audienceDao.get(1).getRoomNumber());
	}

	@Test
	void givenDataSetIdOfAudience_whenGet_thenExpectedCapacityOfAudienceReturned() {
		assertEquals(87, audienceDao.get(1).getCapacity());
	}

	@Test
	void givenDataSetExpectedAudience_whenUpdate_thenRelevantRoomNumberOfAudenceUpdated() {
		audienceDao.update(new Audience(3, 999, 1000));

		long actual = template.queryForObject("SELECT room_number FROM audiences WHERE id=2", Long.class);
		assertEquals(343, actual);
	}

	@Test
	void givenDataSetExpectedAudience_whenUpdate_thenRelevantCapacityOfAudenceUpdated() {
		audienceDao.update(new Audience(3, 999, 1000));

		long actual = template.queryForObject("SELECT capacity FROM audiences WHERE id=3", Long.class);
		assertEquals(1000, actual);
	}
	
	@Test
	void givenDataSetExpectedAudience_whenGetAll_thenExpectedCountOfAudiencesReturned() {
		assertEquals(4, audienceDao.getAll().size());
	}
}
