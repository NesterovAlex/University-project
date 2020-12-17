package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Group;
import com.nesterov.university.model.Student;
import com.nesterov.university.dao.TestConfig;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
class GroupDaoTest {

	@Autowired
	private GroupDao groupDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void givenExpectedCountOfRowsInTable_whenCreate_thenEqualsCountOfRowsReturned() {
		int expected = countRowsInTable(jdbcTemplate, "groups") + 1;

		groupDao.create(new Group("B-12"));

		int actual = countRowsInTable(jdbcTemplate, "groups");
		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedGroup_whenGet_thenRelevantGroupReturned() {
		Group expected = new Group(1, "G-45");
		Student student = new Student("Bob", "Sincler", LocalDate.of(2012, 9, 17), "Toronto", "bob@sincler",
				"987654321", Gender.MALE);
		student.setFaculty("Biology");
		student.setCourse("Biology");
		student.setId(1);
		student.setGroupId(1);

		assertEquals(expected, groupDao.get(1));
	}

	@Test
	void givenNameOfExistingGroup_whenUpdate_thenExpectedNameOfGroupReturned() {
		Group group = new Group(3, "B-12");

		groupDao.update(group);

		String actual = jdbcTemplate.queryForObject("SELECT name FROM groups WHERE id=3", String.class);
		assertEquals(group.getName(), actual);
	}

	@Test
	void givenExpectedCountRowsInTable_whenFindAll_thenEqualCountOfRowsReturned() {
		assertEquals(countRowsInTable(jdbcTemplate, "groups"), groupDao.findAll().size());
	}

	@Test
	void givenExistingIdOfLesson_whenGetAllByLesson_thenExpectedCountOfGroupsReturned() {
		int expected = 2;

		assertEquals(expected, groupDao.findByLessonId(3).size());
	}

	@Test
	void givenExpectedCountOfRowsInTable_whenDelete_thenEqualCountRowsReturned() {
		int expected = countRowsInTable(jdbcTemplate, "groups") - 1;

		groupDao.delete(3);

		int actual = countRowsInTable(jdbcTemplate, "groups");
		assertEquals(expected, actual);
	}

	@Test
	void givenExistingGroup_whenFindByName_thenExpectedGroupReturned() {
		Group expected = new Group(4, "E-34");

		Group actual = groupDao.findByName(expected.getName());

		assertEquals(expected, actual);
	}

	@Test
	void givenNameOfNonExistingGroup_whenFindByName_thenNullReturned() {
		Group expected = new Group(3, "T-5");

		Group actual = groupDao.findByName(expected.getName());

		assertNull(actual);
	}
}
