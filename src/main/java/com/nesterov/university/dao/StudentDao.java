package com.nesterov.university.dao;

import static java.sql.Date.valueOf;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.mapper.StudentRowMapper;
import com.nesterov.university.model.Student;

@Component
public class StudentDao {

	private static final String INSERT = "INSERT INTO students (first_name, last_name, birth_date, address, email, phone, gender) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT = "SELECT *  FROM students WHERE id = ?";
	private static final String UPDATE = "UPDATE students SET first_name = ?, last_name = ?, birth_date = ?, address = ?, email = ?, phone = ?, gender = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM students WHERE id = ?";

	private JdbcTemplate template;

	public StudentDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(Student student) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, student.getFirstName());
			statement.setString(2, student.getLastName());
			statement.setDate(3, valueOf(student.getBithDate()));
			statement.setString(4, student.getAddress());
			statement.setString(5, student.getEmail());
			statement.setString(6, student.getPhone());
			statement.setString(7, student.getGender());
			return statement;
		}, holder);
		student.setId(holder.getKey().longValue());
	}

	public Student get(long id) {
		return template.queryForObject(SELECT, new Object[] { id }, new StudentRowMapper());
	}

	public void delete(long id) {
		template.update(DELETE, id);
	}

	public void update(Student student) {
		template.update(UPDATE, student.getFirstName(), student.getLastName(), student.getBithDate(),
				student.getAddress(), student.getEmail(), student.getPhone(), student.getGender(), student.getId());
	}
}