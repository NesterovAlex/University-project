package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.mapper.TeacherRowMapper;
import com.nesterov.university.model.Teacher;
import static java.sql.Date.valueOf;

@Component
public class TeacherDao {

	private static final String INSERT = "INSERT INTO teachers (first_name, last_name, birth_date, address, email, phone, gender) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT = "SELECT *  FROM teachers WHERE id = ?";
	private static final String UPDATE = "UPDATE teachers SET first_name = ?, last_name = ?, birth_date = ?, address = ?, email = ?, phone = ?, gender = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM teachers WHERE id = ?";

	private JdbcTemplate template;

	public TeacherDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(Teacher teacher) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, teacher.getFirstName());
			statement.setString(2, teacher.getLastName());
			statement.setDate(3, valueOf(teacher.getBithDate()));
			statement.setString(4, teacher.getAddress());
			statement.setString(5, teacher.getEmail());
			statement.setString(6, teacher.getPhone());
			statement.setString(7, teacher.getGender());
			return statement;
		}, holder);
		teacher.setId(holder.getKey().longValue());
	}

	public Teacher getTeacher(long id) {
		return template.queryForObject(SELECT, new Object[] { id }, new TeacherRowMapper());
	}

	public void delete(long id) {
		template.update(DELETE, id);
	}

	public void update(Teacher teacher) {
		template.update(UPDATE, teacher.getFirstName(), teacher.getLastName(), teacher.getBithDate(),
				teacher.getAddress(), teacher.getEmail(), teacher.getPhone(), teacher.getGender(), teacher.getId());
	}
}