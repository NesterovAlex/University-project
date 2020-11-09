package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.mapper.StudentRowMapper;
import com.nesterov.university.model.Student;

@Component
public class StudentDao {

	private static final String SELECT_BY_GROUP = "SELECT * FROM students WHERE group_id = ?";
	private static final String SELECT_BY_ID = "SELECT *  FROM students WHERE id = ?";
	private static final String SELECT = "SELECT * FROM students";
	private static final String INSERT = "INSERT INTO students (group_id, first_name, last_name, birth_date, address, email, phone, gender) values (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE students SET first_name = ?, last_name = ?, birth_date = ?, address = ?, email = ?, phone = ?, gender = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM students WHERE id = ?";

	private JdbcTemplate template;

	@Autowired
	public StudentDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(Student student) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setLong(1, student.getGroupId());
			statement.setString(2, student.getFirstName());
			statement.setString(3, student.getLastName());
			statement.setDate(4, student.getBithDate());
			statement.setString(5, student.getAddress());
			statement.setString(6, student.getEmail());
			statement.setString(7, student.getPhone());
			statement.setString(8, student.getGender().name());
			return statement;
		}, holder);
		student.setId(holder.getKey().longValue());
	}

	public Student get(long id) {
		return template.queryForObject(SELECT_BY_ID, new Object[] { id }, new StudentRowMapper());
	}

	public void delete(long id) {
		template.update(DELETE, id);
	}

	public void update(Student student) {
		template.update(UPDATE, student.getFirstName(), student.getLastName(), student.getBithDate(),
				student.getAddress(), student.getEmail(), student.getPhone(), student.getGender().name(), student.getId());
	}
	
	public List<Student> getAll() {
		return template.query(SELECT, new StudentRowMapper());
	}
	
	public List<Student> getAllByGroup(long id) {
		return template.query(SELECT_BY_GROUP, new Object[] { id }, new StudentRowMapper());
	}
}
