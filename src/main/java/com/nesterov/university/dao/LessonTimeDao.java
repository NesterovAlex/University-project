package com.nesterov.university.dao;

import static java.sql.Time.valueOf;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeDao {

	private static final String INSERT = "INSERT INTO lessonTimes (order_Number, start_lesson, end_lesson) values (?, ?, ?)";
	private static final String SELECT = "SELECT *  FROM lessonTimes WHERE id = ?";
	private static final String UPDATE = "UPDATE lessonTimes SET order_number = ?, start_lesson = ?, end_lesson = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM lessonTimes WHERE id = ?";

	private JdbcTemplate template;

	public LessonTimeDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(LessonTime lessonTime) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setInt(1, lessonTime.getOrderNumber());
			statement.setTime(2, valueOf(lessonTime.getStart()));
			statement.setTime(3, valueOf(lessonTime.getEnd()));
			return statement;
		}, holder);
		lessonTime.setId(holder.getKey().longValue());
	}

	public LessonTime get(long id) {
		return template.queryForObject(SELECT, new Object[] { id },
				(resultSet, rowNum) -> new LessonTime(resultSet.getLong("id"), resultSet.getInt("order_number"),
						resultSet.getTime("start_lesson").toLocalTime(),
						resultSet.getTime("end_lesson").toLocalTime()));
	}

	public void delete(long id) {
		template.update(DELETE, id);
	}

	public void update(LessonTime lessonTime) {
		template.update(UPDATE, lessonTime.getOrderNumber(), lessonTime.getStart(), lessonTime.getEnd(),
				lessonTime.getId());
	}
}
