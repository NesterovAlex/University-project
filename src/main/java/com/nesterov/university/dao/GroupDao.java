package com.nesterov.university.dao;

import static java.util.Optional.of;
import static java.util.Optional.empty;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
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

	private static final Logger log = LoggerFactory.getLogger(GroupDao.class);

	private static final String SELECT_FROM_LESSONS_GROUPS = "SELECT *  FROM groups LEFT JOIN lessons_groups ON lessons_groups.group_id = groups.id WHERE lesson_id = ?";
	private static final String SELECT_BY_NAME = "SELECT *  FROM groups WHERE name = ?";
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
		log.debug("Create {}", group);
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, group.getName());
			return statement;
		}, holder);
		group.setId(holder.getKey().longValue());
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Optional<Group> get(long id) {
		log.debug("Get group by id={}", id);
		try {
			return of(jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, groupRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}

	@Transactional
	public void delete(long id) {
		log.debug("Delete group by id={}", id);
		jdbcTemplate.update(DELETE, id);
	}

	@Transactional
	public void update(Group group) {
		log.debug("Update group by id={}", group);
		jdbcTemplate.update(UPDATE, group.getName(), group.getId());
	}

	public List<Group> findAll() {
		log.debug("Find all groups");
		return jdbcTemplate.query(SELECT, groupRowMapper);
	}

	public List<Group> findByLessonId(long id) {
		log.debug("Find groups by id={}", id);
		return jdbcTemplate.query(SELECT_FROM_LESSONS_GROUPS, new Object[] { id }, groupRowMapper);
	}

	public Optional<Group> findByName(String name) {
		log.debug("Find group by name={}", name);
		try {
			return of(jdbcTemplate.queryForObject(SELECT_BY_NAME, new Object[] { name }, groupRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}
}
