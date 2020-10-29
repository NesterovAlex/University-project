package com.nesterov.university.dao;

import static java.sql.Time.valueOf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.nesterov.university.model.LessonTime;

public class LessonTimeDao {

	private static final String INSERT = "INSERT INTO lessonTimes (order_Number, start_lesson, end_lesson) values (?, ?, ?)";
	private static final String SELECT = "SELECT *  FROM lessonTimes WHERE id = ?";
	private static final String UPDATE = "UPDATE lessonTimes SET order_number = ?, start_lesson = ?, end_lesson = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM lessonTimes WHERE id = ?";

	private JdbcTemplate template;

	public LessonTimeDao(DataSource source) {
		this.template = new JdbcTemplate(source);
	}

	public long create(LessonTime lessonTime) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setInt(1, lessonTime.getOrderNumber());
			statement.setTime(2, valueOf(lessonTime.getStart()));
			statement.setTime(3, valueOf(lessonTime.getEnd()));
			return statement;
		}, holder);
		lessonTime.setId(holder.getKey().longValue());
		return lessonTime.getId();
	}

	public LessonTime get(long id) {
		return template.queryForObject(SELECT, new Object[] { id },
				(resultSet, rowNum) -> new LessonTime(resultSet.getLong("id"), resultSet.getInt("order_number"),
						resultSet.getTime("start_lesson").toLocalTime(),
						resultSet.getTime("end_lesson").toLocalTime()));
	}

	public boolean delete(long id) {
		return template.update(DELETE, id) == 1;
	}

	public long update(LessonTime lessonTime) {
		return template.update(UPDATE, lessonTime.getOrderNumber(), lessonTime.getStart(), lessonTime.getEnd(),
				lessonTime.getId());
	}
}
