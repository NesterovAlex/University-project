package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.mapper.StudentRowMapper;
import com.nesterov.university.model.Student;

@Component
public class StudentDao {

	private static final String SELECT_BY_GROUP = "SELECT * FROM students WHERE group_id = ?";
	private static final String SELECT_BY_ID = "SELECT *  FROM students WHERE id = ?";
	private static final String SELECT_BY_EMAIL = "SELECT *  FROM students WHERE email = ?";
	private static final String SELECT_BY_PHONE = "SELECT *  FROM students WHERE phone = ?";
	private static final String SELECT_BY_ADDRESS = "SELECT *  FROM students WHERE address = ?";
	private static final String SELECT = "SELECT * FROM students";
	private static final String INSERT = "INSERT INTO students (group_id, first_name, last_name, birth_date, address, email, phone, gender, faculty, course) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE students SET first_name = ?, last_name = ?, birth_date = ?, address = ?, email = ?, phone = ?, gender = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM students WHERE id = ?";

	private JdbcTemplate jdbcTemplate;
	private StudentRowMapper studentRowMapper;

	public StudentDao(JdbcTemplate template, StudentRowMapper studentRowMapper) {
		this.jdbcTemplate = template;
		this.studentRowMapper = studentRowMapper;
	}

	public void create(Student student) {
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setLong(1, student.getGroupId());
			statement.setString(2, student.getFirstName());
			statement.setString(3, student.getLastName());
			statement.setObject(4, student.getBithDate());
			statement.setString(5, student.getAddress());
			statement.setString(6, student.getEmail());
			statement.setString(7, student.getPhone());
			statement.setString(8, student.getGender().name());
			statement.setString(9, student.getFaculty());
			statement.setString(10, student.getCourse());
			return statement;
		}, holder);
		student.setId(holder.getKey().longValue());
	}

	public Student get(long id) {
		Student student = new Student();
		try {
			student = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			e.getMessage();
		}
		return student;
	}

	public void delete(long id) {
		jdbcTemplate.update(DELETE, id);
	}

	public void update(Student student) {
		jdbcTemplate.update(UPDATE, student.getFirstName(), student.getLastName(), student.getBithDate(),
				student.getAddress(), student.getEmail(), student.getPhone(), student.getGender().name(),
				student.getId());
	}

	public List<Student> findAll() {
		return jdbcTemplate.query(SELECT, studentRowMapper);
	}

	public List<Student> findByGroupId(long id) {
		return jdbcTemplate.query(SELECT_BY_GROUP, new Object[] { id }, studentRowMapper);
	}

	public List<Student> findByEmail(String email) {
		List<Student> students = null;
		try {
			students = jdbcTemplate.query(SELECT_BY_EMAIL, new Object[] { email }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			e.getMessage();
		}
		return students;
	}

	public List<Student> findByPhone(String phone) {
		List<Student> students = null;
		try {
			students = jdbcTemplate.query(SELECT_BY_PHONE, new Object[] { phone }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			e.getMessage();
		}
		return students;
	}

	public List<Student> findByAddress(String address) {
		List<Student> students = null;
		try {
			students = jdbcTemplate.query(SELECT_BY_ADDRESS, new Object[] { address }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			e.getMessage();
		}
		return students;
	}
}
