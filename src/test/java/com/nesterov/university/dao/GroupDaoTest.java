package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.nesterov.university.model.Group;

class GroupDaoTest {

	private GroupDao dao;

	@BeforeEach
	void setUp() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/groups_data.sql").build();
		dao = new GroupDao(dataSource);
	}

	@Test
	public void givenExpectedData_whenCreate_thenReturnExpectedId() {

		assertEquals(5, dao.create(new Group("B-12")));
	}

	@Test
	void givenDataSetAndIdOfGroup_whenRead_thenExpectedIdOfGroupReturned() {

		assertEquals(1, dao.get(1).getId());
	}

	@Test
	void givenDataSetAndIdOfGroup_whenRead_thenExpectedNameOfGroupReturned() {

		assertEquals("G-45", dao.get(1).getName());
	}

	@Test
	void givenDataSet_whenDelete_thenTrueOfDeleteReturned() {

		assertTrue(dao.delete(3));
	}

	@Test
	void givenDataSetExpectedGroup_whenUpdate_thenRelevantParametersOfGroupUpdated() {
		
		assertEquals(1, dao.update(new Group(3, "B-12")));
		
		assertEquals("B-12", dao.get(3).getName());
	}


}
