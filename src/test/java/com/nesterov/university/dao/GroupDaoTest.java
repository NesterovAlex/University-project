package com.nesterov.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Group;
import com.nesterov.university.model.Student;
import com.nesterov.university.dao.TestConfig;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(SpringExtension.class)
@Sql(scripts = { "/schema.sql", "classpath:test_data.sql" })
class GroupDaoTest {

	@Autowired
	private GroupDao groupDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void givenExpectedData_whenCreate_thenDifferentCountOfRowsBeforAndAfterCreatingReturned() {
		int countRowsbeforeCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

		groupDao.create(new Group("B-12"));

		int countRowsafterCreate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
		assertFalse(countRowsbeforeCreate == countRowsafterCreate);
	}

	@Test
	void givenDataSet_whenGet_thenExpectedGroupReturned() {
		Group expected = new Group(1, "G-45");
		Student student = new Student("Bob", "Sincler", LocalDate.of(2012, 9, 17), "Toronto", "bob@sincler",
				"987654321", Gender.MALE);
		student.setFaculty("Biology");
		student.setCourse("Biology");
		student.setId(1);
		student.setGroupId(1);
		List<Student> students = new ArrayList<Student>();
		students.add(student);
		assertEquals(expected, groupDao.get(1));
	}

	@Test
	void givenDataSetExpectedGroup_whenUpdate_thenRelevantParametersOfGroupUpdated() {
		groupDao.update(new Group(3, "B-12"));

		String actual = jdbcTemplate.queryForObject("SELECT name FROM groups WHERE id=3", String.class);
		assertEquals("B-12", actual);
	}

	@Test
	void givenDataSetExpectedGroup_whenGetAll_thenExpectedCountOfGroupsReturned() {
		assertEquals(4, groupDao.getAll().size());
	}

	@Test
	void givenDataSetExpectedGroup_whenGetAllByLesson_thenExpectedCountOfGroupsReturned() {
		assertEquals(2, groupDao.findByLessonId(3).size());
	}

	@Test
	void givenDataSet_whenDelete_thenDifferentCountRowsBeforAndAfterDeletingReturned() {
		int beforeDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

		groupDao.delete(3);

		int afterDelete = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
		assertFalse(beforeDelete == afterDelete);
	}
}
