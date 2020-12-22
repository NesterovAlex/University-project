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
import com.nesterov.university.dao.mapper.LessonTimeRowMapper;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeDao {

	private static final Logger log = LoggerFactory.getLogger(LessonTimeDao.class);

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
		log.debug("Create {}", lessonTime);
		final KeyHolder holder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
				statement.setInt(1, lessonTime.getOrderNumber());
				statement.setObject(2, lessonTime.getStart());
				statement.setObject(3, lessonTime.getEnd());
				return statement;
			}, holder);
			lessonTime.setId(holder.getKey().longValue());
		} catch (DataAccessException e) {
			String message = format("LessonTime '%s' not created ", lessonTime);
			throw new NotCreateException(message, e);
		}
	}

	public LessonTime get(long id) throws EntityNotFoundException {
		log.debug("Get lessonTime by id={}", id);
		LessonTime lessonTime = null;
		try {
			lessonTime = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, lessonTimeRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("LessonTime with id '%s' not found", id);
			throw new EntityNotFoundException(message, e);
		}
		return lessonTime;
	}

	public void delete(long id) throws NotExistException {
		log.debug("Delete LessonTime by id={}", id);
		try {
			jdbcTemplate.update(DELETE, id);
		} catch (DataAccessException e) {
			String message = format("LessonTime with id = '%s' not exist ", id);
			throw new NotExistException(message, e);
		}
	}

	public void update(LessonTime lessonTime) throws NotCreateException {
		log.debug("Update LessonTime {}", lessonTime);
		try {
			jdbcTemplate.update(UPDATE, lessonTime.getOrderNumber(), lessonTime.getStart(), lessonTime.getEnd(),
					lessonTime.getId());
		} catch (DataAccessException e) {
			String message = format("LessonTime '%s' not updated", lessonTime);
			throw new NotCreateException(message, e);
		}
	}

	public List<LessonTime> findAll() throws EntityNotFoundException {
		log.debug("Find all LessonTimes");
		List<LessonTime> lessonTimes = null;
		try {
			lessonTimes = jdbcTemplate.query(SELECT, lessonTimeRowMapper);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("No LessonTimes", e);
		}
		return lessonTimes;
	}
}
