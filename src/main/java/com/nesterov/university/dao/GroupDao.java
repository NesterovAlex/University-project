package com.nesterov.university.dao;

import static java.lang.String.format;

import java.sql.PreparedStatement;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.dao.mapper.GroupRowMapper;
import com.nesterov.university.model.Group;

@Component
public class GroupDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupDao.class);

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
	public void create(Group group) throws NotCreateException {
		LOGGER.debug("Creating '{}'", group);
		final KeyHolder holder = new GeneratedKeyHolder();
		int affectedRows = jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, group.getName());
			return statement;
		}, holder);
		group.setId(holder.getKey().longValue());
		if (affectedRows == 0) {
			LOGGER.error("Group not created");
			String message = format("Group '%s' not created ", group);
			throw new NotCreateException(message);
		} else {
			LOGGER.trace("Successfully created '{}'", group);
		}
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Group get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting group by id = '{}'", id);
		Group group = new Group();
		try {
			group = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, groupRowMapper);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(group.toString());
			String message = format("Group with id '%s' not found", id);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			LOGGER.error(group.toString());
			String message = format("Unable to get Group with id '%s'", id);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Founded '{}'", group);
		return group;
	}

	@Transactional
	public void delete(long id) throws NotExistException {
		LOGGER.debug("Deleting group by id = '{}'", id);
		int affectedRows = jdbcTemplate.update(DELETE, id);
		if (affectedRows == 0) {
			LOGGER.error("Group was not deleted");
			String message = format("Group with id = '%s' not exist ", id);
			throw new NotExistException(message);
		} else {
			LOGGER.trace("Deleted Group with id = '{}'", id);
		}
	}

	@Transactional
	public void update(Group group) throws NotCreateException {
		LOGGER.debug("Updating group by id = '{}'", group);
		int affectedRows = jdbcTemplate.update(UPDATE, group.getName(), group.getId());
		if (affectedRows == 0) {
			LOGGER.error("Group was not created");
			String message = format("Audience '%s' not updated", group);
			throw new NotCreateException(message);
		} else {
			LOGGER.trace("Updated '{}'", group);
		}
	}

	public List<Group> findAll() throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting all groups");
		List<Group> groups = null;
		try {
			groups = jdbcTemplate.query(SELECT, groupRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = "No groups";
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = "Unable to get Groups";
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all groups");
		return groups;
	}

	public List<Group> findByLessonId(long id) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting groups by id = '{}'", id);
		List<Group> groups = null;
		try {
			groups = jdbcTemplate.query(SELECT_FROM_LESSONS_GROUPS, new Object[] { id }, groupRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Groups with lessonId '%s' not found", id);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Groups with lessonId '%s'", id);
			LOGGER.error(message, id);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all groups by lessonId");
		return groups;
	}

	public Group findByName(String name) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting group by name = '{}'", name);
		Group group = null;
		try {
			group = jdbcTemplate.queryForObject(SELECT_BY_NAME, new Object[] { name }, groupRowMapper);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("Audience not found");
			String message = format("group with name '%s' not found", name);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			LOGGER.error("Audience not found");
			String message = format("Unable to get Audience with id '%s'", name);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded group by name");
		return group;
	}
}
