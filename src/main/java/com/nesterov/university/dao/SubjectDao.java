package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.model.Subject;

@Component
public class SubjectDao {

	private static final String INSERT = "INSERT INTO subjects (name) values (?)";
	private static final String SELECT = "SELECT *  FROM subjects WHERE id = ?";
	private static final String UPDATE = "UPDATE subjects SET name = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM subjects WHERE id = ?";

	private JdbcTemplate template;

	public SubjectDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(Subject subject) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, subject.getName());
			return statement;
		}, holder);
		subject.setId(holder.getKey().longValue());
	}

	public Subject getSubject(long id) {
		return template.queryForObject(SELECT, new Object[] { id },
				(resultSet, rowNum) -> new Subject(resultSet.getLong("id"), resultSet.getString("name")));
	}

	public void delete(long id) {
		template.update(DELETE, id);
	}

	public void update(Subject subject) {
		template.update(UPDATE, subject.getName(), subject.getId());
	}
}
