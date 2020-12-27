package com.nesterov.university.dao;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
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
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotUpdateException;
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
		} catch (Exception e) {
			String message = format("LessonTime '%s' not created ", lessonTime);
			throw new NotCreateException(message);
		}
	}

	public Optional<LessonTime> get(long id) {
		log.debug("Get lessonTime by id={}", id);
		try {
			return ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, lessonTimeRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}

	public void delete(long id) throws NotDeleteException {
		log.debug("Delete LessonTime by id={}", id);
		int affectedRows = jdbcTemplate.update(DELETE, id);
		if (affectedRows == 0) {
			String message = format("LessonTime with id = '%s' not deleted", id);
			throw new NotDeleteException(message);
		}
	}

	public void update(LessonTime lessonTime) {
		log.debug("Update LessonTime {}", lessonTime);
		int affectedRows = jdbcTemplate.update(UPDATE, lessonTime.getOrderNumber(), lessonTime.getStart(),
				lessonTime.getEnd(), lessonTime.getId());
		if (affectedRows == 0) {
			String message = format("LessonTime '%s' not updated", lessonTime);
			throw new NotUpdateException(message);
		}
	}

	public List<LessonTime> findAll() {
		log.debug("Find all LessonTimes");
		return jdbcTemplate.query(SELECT, lessonTimeRowMapper);
	}
}
