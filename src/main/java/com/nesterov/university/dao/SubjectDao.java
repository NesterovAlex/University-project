package com.nesterov.university.dao;

import java.sql.PreparedStatement;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Subject;

@Component
public class SubjectDao {

	private static final String INSERT = "INSERT INTO subjects (name) values (?)";
	private static final String SELECT = "SELECT *  FROM subjects WHERE id = ?";
	private static final String UPDATE = "UPDATE subjects SET name = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM subjects WHERE id = ?";

	private JdbcTemplate template;

	public SubjectDao(DataSource source) {
		this.template = new JdbcTemplate(source);
	}

	public long create(Subject subject) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, subject.getName());
			return statement;
		}, holder);
		subject.setId(holder.getKey().longValue());
		return subject.getId();
	}

	public Subject getSubject(long id) {
		return template.queryForObject(SELECT, new Object[] { id },
				(resultSet, rowNum) -> new Subject(resultSet.getLong("id"), resultSet.getString("name")));
	}

	public boolean delete(long id) {
		return template.update(DELETE, id) == 1;
	}

	public long update(Subject subject) {
		return template.update(UPDATE, subject.getName(), subject.getId());
	}
}
