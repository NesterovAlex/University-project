package com.nesterov.university.dao;

import static java.sql.Date.valueOf;

import java.sql.PreparedStatement;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.format.datetime.joda.LocalDateParser;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.time.LocalDate;
import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Lesson;
import com.nesterov.university.model.LessonTime;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

public class LessonDao {

	private static final String INSERT = "INSERT INTO lessons (audience, subject, teacher, lesson_time, lesson_date) values (?, ?, ?, ?, ?)";
	private static final String SELECT = "SELECT *  FROM lessons WHERE id = ?";
	private static final String UPDATE = "UPDATE lessons SET audience = ?, subject = ?, teacher = ?, lesson_time = ?, lesson_date = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM lessons WHERE id = ?";

	private JdbcTemplate template;

	public LessonDao(DataSource source) {
		this.template = new JdbcTemplate(source);
	}

	public long create(Lesson lesson) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] {"id"});
			statement.setLong(1, lesson.getAudience().getId());
			statement.setLong(2, lesson.getSubject().getId());
			statement.setLong(3, lesson.getTeacher().getId());
			statement.setLong(4, lesson.getTime().getId());
			statement.setDate(5, valueOf(lesson.getDate()));
			return statement;
		}, holder);
		lesson.setId(holder.getKey().longValue());
		return lesson.getId();
	}

	public Lesson get(long id) {
		 return template.queryForObject(SELECT, new Object[]{id}, new LessonRowMapper());
	}

	public boolean delete(long id) {
		return template.update(DELETE, id) == 1;
	}

	public long update(Lesson lesson) {
		return template.update(UPDATE, lesson.getAudience().getId(), lesson.getSubject().getId(),
				lesson.getTeacher().getId(), lesson.getTime().getId(), lesson.getDate(), lesson.getId());
	}
}
