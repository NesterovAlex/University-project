package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.nesterov.university.model.Group;
import com.nesterov.university.configuration.ApplicationConfig;
import com.nesterov.university.dao.TestConfig;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class GroupDaoTest {

	@Autowired
	private GroupDao dao;
	@Autowired
	private JdbcTemplate template;

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedCountOfAudiencesInDataBase() {
		dao.create(new Group("B-12"));

		assertEquals(5, JdbcTestUtils.countRowsInTable(template, "groups"));
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
	void givenDataSetExpectedGroup_whenUpdate_thenRelevantParametersOfGroupUpdated() {
		dao.update(new Group(3, "B-12"));

		String actual = template.queryForObject("SELECT name FROM groups WHERE id=3", String.class);
		assertEquals("B-12", actual);
	}
	
	@Test
	void givenDataSetExpectedGroup_whenGetAll_thenExpectedCountOfGroupsReturned() {
		assertEquals(4, dao.getAll().size());
	}
	
	@Test
	void givenDataSetExpectedGroup_whenGetAllByLesson_thenExpectedCountOfGroupsReturned() {
		assertEquals(2, dao.getAllByLesson(3).size());
	}

	@Test
	void givenDataSet_whenDelete_thenExpectedCountOfGroupsReturned() {
		dao.delete(3);

		assertEquals(4, JdbcTestUtils.countRowsInTable(template, "groups"));
	}
}
