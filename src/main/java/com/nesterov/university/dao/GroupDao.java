package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.nesterov.university.dao.mapper.GroupRowMapper;
import com.nesterov.university.model.Group;

@Component
public class GroupDao {

	private static final String SELECT_FROM_LESSONS_GROUPS = "SELECT *  FROM groups LEFT JOIN lessons_groups ON lessons_groups.group_id = groups.id WHERE lesson_id = ?";
	private static final String SELECT_BY_ID = "SELECT *  FROM groups WHERE id = ?";
	private static final String SELECT = "SELECT * FROM groups";
	private static final String INSERT = "INSERT INTO groups (name) VALUES (?)";
	private static final String UPDATE = "UPDATE groups SET name = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM groups WHERE id = ?";

	private JdbcTemplate jdbcTemplate;
	private GroupRowMapper groupRowMapper;

	public GroupDao(JdbcTemplate template, GroupRowMapper groupRowMapper) {
		this.jdbcTemplate = template;
		this.groupRowMapper = groupRowMapper;
	}

	@Transactional
	public void create(Group group) {
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, group.getName());
			return statement;
		}, holder);
		group.setId(holder.getKey().longValue());
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Group get(long id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, groupRowMapper);
	}

	@Transactional
	public void delete(long id) {
		jdbcTemplate.update(DELETE, id);
	}

	@Transactional
	public void update(Group group) {
		jdbcTemplate.update(UPDATE, group.getName(), group.getId());
	}

	public List<Group> findAll() {
		return jdbcTemplate.query(SELECT, groupRowMapper);
	}

	public List<Group> findByLessonId(long id) {
		return jdbcTemplate.query(SELECT_FROM_LESSONS_GROUPS, new Object[] { id }, groupRowMapper);
	}
}
