package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.mapper.LessonTimeRowMapper;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeDao {

	private static final String SELECT_BY_ID = "SELECT *  FROM lesson_times WHERE id = ?";
	private static final String SELECT = "SELECT * FROM lesson_time";
	private static final String INSERT = "INSERT INTO lesson_times (order_Number, start_lesson, end_lesson) values (?, ?, ?)";
	private static final String UPDATE = "UPDATE lesson_times SET order_number = ?, start_lesson = ?, end_lesson = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM lesson_times WHERE id = ?";

	private JdbcTemplate jdbcTemplate;
	private LessonTimeRowMapper lessonTimeRowMapper;

	public LessonTimeDao(JdbcTemplate template, LessonTimeRowMapper lessonTimeRowMapper) {
		this.jdbcTemplate = template;
		this.lessonTimeRowMapper = lessonTimeRowMapper;
	}

	public void create(LessonTime lessonTime) {
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setInt(1, lessonTime.getOrderNumber());
			statement.setObject(2, lessonTime.getStart());
			statement.setObject(3, lessonTime.getEnd());
			return statement;
		}, holder);
		lessonTime.setId(holder.getKey().longValue());
	}

	public LessonTime get(long id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, lessonTimeRowMapper);
	}

	public void delete(long id) {
		jdbcTemplate.update(DELETE, id);
	}

	public void update(LessonTime lessonTime) {
		jdbcTemplate.update(UPDATE, lessonTime.getOrderNumber(), lessonTime.getStart(), lessonTime.getEnd(),
				lessonTime.getId());
	}

	public List<LessonTime> getAll() {
		return jdbcTemplate.query(SELECT, lessonTimeRowMapper);
	}
}
