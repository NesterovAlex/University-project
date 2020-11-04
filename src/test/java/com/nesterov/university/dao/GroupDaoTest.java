package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Group;

class GroupDaoTest {

	private GroupDao dao;
	private JdbcTemplate template;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/groups_data.sql").build();
		template = new JdbcTemplate(dataSource);
		dao = new GroupDao(template);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedCountOfAudiencesInDataBase() {
		dao.create(new Group("B-12"));

		long actual = template.queryForObject("SELECT COUNT(*) FROM groups", Long.class);
		assertEquals(5, actual);
	}

	@Test
	public void givenExpectedData_whenCreate_thenExpectedRoomNumberOfAudienceReturned() {

		dao.create(new Group("B-12"));

		String actual = template.queryForObject("SELECT name FROM groups WHERE id=5", String.class);
		assertEquals("B-12", actual);
	}

	@Test
	void givenDataSetAndIdOfGroup_whenGet_thenExpectedIdOfGroupReturned() {
		assertEquals(1, dao.get(1).getId());
	}

	@Test
	void givenDataSetAndIdOfGroup_whenGet_thenExpectedNameOfGroupReturned() {
		assertEquals("G-45", dao.get(1).getName());
	}

	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfGroupsReturned() {
		dao.delete(3);

		long actual = template.queryForObject("SELECT COUNT(*) FROM groups", Long.class);
		assertEquals(3, actual);
	}

	@Test
	void givenDataSetExpectedGroup_whenUpdate_thenRelevantParametersOfGroupUpdated() {
		dao.update(new Group(3, "B-12"));

		String actual = template.queryForObject("SELECT name FROM groups WHERE id=3", String.class);
		assertEquals("B-12", actual);
	}

}
