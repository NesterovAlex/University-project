package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.nesterov.university.model.Audience;

class AudienceDaoTest {

	private AudienceDao dao;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/audience_data.sql").build();
		dao = new AudienceDao(dataSource);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {

		assertEquals(5, dao.create(new Audience(14, 87)));
	}

	@Test
	void givenDataSetAndIdOfAudience_whenRead_thenExpectedIdOfAudienceReturned() {

		assertEquals(1, dao.get(1).getId());
	}

	@Test
	void givenDataSetIdOfAudience_whenRead_thenExpectedRoomNumberOfAudienceReturned() {

		assertEquals(14, dao.get(1).getRoomNumber());
	}

	@Test
	void givenDataSetIdOfAudience_whenRead_thenExpectedCapacityOfAudienceReturned() {

		assertEquals(87, dao.get(1).getCapacity());
	}

	@Test
	void givenDataSet_whenDeleteAudience_thenTrueOfDeleteReturned() {

		assertTrue(dao.delete(3));
	}

	@Test
	void givenDataSetExpectedAudience_whenUpdate_thenRelevantParametersOfAudenceUpdated() {
		
		assertEquals(1, dao.update(new Audience(3, 999, 1000)));
		
		assertEquals(999, dao.get(3).getRoomNumber());
	}

}
