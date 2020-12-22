package com.nesterov.university.dao;

import static java.lang.String.format;

import java.sql.PreparedStatement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.mapper.StudentRowMapper;
import com.nesterov.university.model.Student;

@Component
public class StudentDao {

	private static final Logger log = LoggerFactory.getLogger(StudentDao.class);

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

	public void create(Student student) throws NotCreateException {
		log.debug("Create {}", student);
		final KeyHolder holder = new GeneratedKeyHolder();
		try {
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
		} catch (DataAccessException e) {
			String message = format("Student '%s' not created ", student);
			throw new NotCreateException(message, e);
		}
	}

	public Student get(long id) throws EntityNotFoundException {
		log.debug("Get student by id={}", id);
		Student student = null;
		try {
			student = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Student with id '%s' not found", id);
			throw new EntityNotFoundException(message, e);
		}
		return student;
	}

	public void delete(long id) throws NotExistException {
		log.debug("Delete student by id = {}", id);
		try {
			jdbcTemplate.update(DELETE, id);
		} catch (DataAccessException e) {
			String message = format("Student with id = '%s' not exist ", id);
			throw new NotExistException(message, e);
		}
	}

	public void update(Student student) throws NotCreateException {
		log.debug("Update student {}", student);
		try {
			jdbcTemplate.update(UPDATE, student.getFirstName(), student.getLastName(), student.getBithDate(),
					student.getAddress(), student.getEmail(), student.getPhone(), student.getGender().name(),
					student.getId());
		} catch (DataAccessException e) {
			String message = format("Student '%s' not updated", student);
			throw new NotCreateException(message, e);
		}
	}

	public List<Student> findAll() throws EntityNotFoundException {
		log.debug("Get all students");
		List<Student> students = null;
		try {
			students = jdbcTemplate.query(SELECT, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("No Students", e);
		}
		return students;
	}

	public List<Student> findByGroupId(long id) throws EntityNotFoundException {
		log.debug("Get students by groupId={}", id);
		List<Student> students = null;
		try {
			students = jdbcTemplate.query(SELECT_BY_GROUP, new Object[] { id }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Students with groupId = '%s' not found", id);
			throw new EntityNotFoundException(message, e);
		}
		return students;
	}

	public Student findByEmail(String email) throws EntityNotFoundException {
		log.debug("Find student by email={}", email);
		Student student = null;
		try {
			student = jdbcTemplate.queryForObject(SELECT_BY_EMAIL, new Object[] { email }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Student with email = '%s' not found", email);
			throw new EntityNotFoundException(message, e);
		}
		return student;
	}

	public Student findByPhone(String phone) throws EntityNotFoundException {
		log.debug("Find student by phone={}", phone);
		Student student = null;
		try {
			student = jdbcTemplate.queryForObject(SELECT_BY_PHONE, new Object[] { phone }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Student with phone = '%s' not found", phone);
			throw new EntityNotFoundException(message, e);
		}
		return student;
	}

	public Student findByAddress(String address) throws EntityNotFoundException {
		log.debug("Find student by address={}", address);
		Student student = null;
		try {
			student = jdbcTemplate.queryForObject(SELECT_BY_ADDRESS, new Object[] { address }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Student with address = '%s' not found", address);
			throw new EntityNotFoundException(message, e);
		}
		return student;
	}
}
