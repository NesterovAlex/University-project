package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeDao {

	private static final String SELECT_BY_ID = "SELECT *  FROM lesson_times WHERE id = ?";
	private static final String SELECT = "SELECT * FROM lesson_time";
	private static final String INSERT = "INSERT INTO lesson_times (order_Number, start_lesson, end_lesson) values (?, ?, ?)";
	private static final String UPDATE = "UPDATE lesson_times SET order_number = ?, start_lesson = ?, end_lesson = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM lesson_times WHERE id = ?";

	private JdbcTemplate template;

	public LessonTimeDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(LessonTime lessonTime) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setInt(1, lessonTime.getOrderNumber());
			statement.setTime(2, lessonTime.getStart());
			statement.setTime(3, lessonTime.getEnd());
			return statement;
		}, holder);
		lessonTime.setId(holder.getKey().longValue());
	}

	public LessonTime get(long id) {
		return template.queryForObject(SELECT_BY_ID, new Object[] { id },
				(resultSet, rowNum) -> new LessonTime(resultSet.getLong("id"), resultSet.getInt("order_number"),
						resultSet.getTime("start_lesson"),
						resultSet.getTime("end_lesson")));
	}

	public void delete(long id) {
		template.update(DELETE, id);
	}

	public void update(LessonTime lessonTime) {
		template.update(UPDATE, lessonTime.getOrderNumber(), lessonTime.getStart(), lessonTime.getEnd(),
				lessonTime.getId());
	}

	public List<LessonTime> getAll() {
		return template.query(SELECT, (rs, rowNum) -> new LessonTime(rs.getLong("id"), rs.getInt("order_number"),
				rs.getTime("start_lesson"), rs.getTime("end_lesson")));
	}
}
