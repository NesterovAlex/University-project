package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.mapper.LessonRowMapper;
import com.nesterov.university.model.Lesson;

@Component
public class LessonDao {

	private static final String SELECT_BY_ID = "SELECT * FROM lessons WHERE id = ?";
	private static final String SELECT = "SELECT * FROM lessons";
	private static final String INSERT = "INSERT INTO lessons (audience_id, subject_id, teacher_id, lesson_time_id, lesson_date) values (?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE lessons SET audience_id = ?, subject_id = ?, teacher_id = ?, lesson_time_id = ?, lesson_date = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM lessons WHERE id = ?";

	private JdbcTemplate template;

	public LessonDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(Lesson lesson) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setLong(1, lesson.getAudience().getId());
			statement.setLong(2, lesson.getSubject().getId());
			statement.setLong(3, lesson.getTeacher().getId());
			statement.setLong(4, lesson.getTime().getId());
			statement.setDate(5, lesson.getDate());
			return statement;
		}, holder);
		lesson.setId(holder.getKey().longValue());
	}

	public Lesson get(long id) {
		return template.queryForObject(SELECT_BY_ID, new Object[] { id }, new LessonRowMapper());
	}

	public void delete(long id) {
		template.update(DELETE, id);
	}

	public void update(Lesson lesson) {
		template.update(UPDATE, lesson.getAudience().getId(), lesson.getSubject().getId(), lesson.getTeacher().getId(),
				lesson.getTime().getId(), lesson.getDate(), lesson.getId());
	}
	
	public List<Lesson> findAll() {
		return template.query(SELECT, new LessonRowMapper());
	}
}
