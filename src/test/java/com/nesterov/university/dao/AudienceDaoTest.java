package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.nesterov.university.model.Audience;

@SpringJUnitConfig(TestConfig.class)
class AudienceDaoTest {

	private AudienceDao dao;
	private JdbcTemplate template;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/audience_data.sql").build();
		template = new JdbcTemplate(dataSource);
		dao = new AudienceDao(template);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedCountAudiencesInDataBase() {
		dao.create(new Audience(14, 87));

		long actual = template.queryForObject("SELECT COUNT(*) FROM audiences", Long.class);
		assertEquals(5, actual);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedRoomNumberOfAudienceReturned() {
		dao.create(new Audience(14, 87));

		long actual = template.queryForObject("SELECT room_number FROM audiences WHERE id=5", Long.class);
		assertEquals(14, actual);
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

		long actual = template.queryForObject("SELECT COUNT(*) FROM audiences", Long.class);
		assertEquals(3, actual);
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

}
