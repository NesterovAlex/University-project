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

import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.dao.mapper.LessonTimeRowMapper;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(LessonTimeDao.class);

	private static final String SELECT_BY_ID = "SELECT *  FROM lesson_times WHERE id = ?";
	private static final String SELECT = "SELECT * FROM lesson_times";
	private static final String INSERT = "INSERT INTO lesson_times (order_Number, start_lesson, end_lesson) values (?, ?, ?)";
	private static final String UPDATE = "UPDATE lesson_times SET order_number = ?, start_lesson = ?, end_lesson = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM lesson_times WHERE id = ?";

	private JdbcTemplate jdbcTemplate;
	private LessonTimeRowMapper lessonTimeRowMapper;

	public LessonTimeDao(JdbcTemplate template, LessonTimeRowMapper lessonTimeRowMapper) {
		this.jdbcTemplate = template;
		this.lessonTimeRowMapper = lessonTimeRowMapper;
	}

	public void create(LessonTime lessonTime) throws NotCreateException {
		LOGGER.debug("Creating '{}'", lessonTime);
		final KeyHolder holder = new GeneratedKeyHolder();
		int affectedRows = jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setInt(1, lessonTime.getOrderNumber());
			statement.setObject(2, lessonTime.getStart());
			statement.setObject(3, lessonTime.getEnd());
			return statement;
		}, holder);
		lessonTime.setId(holder.getKey().longValue());
		if (affectedRows == 0) {
			String message = format("LessonTime '%s' not created ", lessonTime);
			LOGGER.error(message);
			throw new NotCreateException(message);
		} else {
			LOGGER.trace("Successfully created '{}'", lessonTime);
		}
	}

	public LessonTime get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting lessonTime by id = '{}'", id);
		LessonTime lessonTime = new LessonTime();
		try {
			lessonTime = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, lessonTimeRowMapper);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(lessonTime.toString());
			String message = format("LessonTime with id '%s' not found", id);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			LOGGER.error(lessonTime.toString());
			String message = format("Unable to get LessonTime with id '%s'", id);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Founded '{}'", lessonTime);
		return lessonTime;
	}

	public void delete(long id) throws NotExistException {
		LOGGER.debug("Deleting LessonTime by id = '{}'", id);
		int affectedRows = jdbcTemplate.update(DELETE, id);
		if (affectedRows == 0) {
			LOGGER.error("LessonTime was not deleted");
			String message = format("LessonTime with id = '%s' not exist ", id);
			throw new NotExistException(message);
		} else {
			LOGGER.trace("Deleted LessonTime with id = '{}'", id);
		}
	}

	public void update(LessonTime lessonTime) throws NotCreateException {
		LOGGER.debug("Updating LessonTime '{}'", lessonTime);
		int affectedRows = jdbcTemplate.update(UPDATE, lessonTime.getOrderNumber(), lessonTime.getStart(),
				lessonTime.getEnd(), lessonTime.getId());
		if (affectedRows == 0) {
			LOGGER.error("LessonTime was not updated");
			String message = format("LessonTime '%s' not updated", lessonTime);
			throw new NotCreateException(message);
		} else {
			LOGGER.trace("Updated '{}'", lessonTime);
		}
	}

	public List<LessonTime> findAll() throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting all LessonTimes");
		List<LessonTime> lessonTimes = null;
		try {
			lessonTimes = jdbcTemplate.query(SELECT, lessonTimeRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = "No LessonTimes";
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = "Unable to get LessonTimes";
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all LessonTimes");
		return lessonTimes;
	}
}
