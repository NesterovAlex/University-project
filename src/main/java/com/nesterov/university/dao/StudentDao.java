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
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.dao.mapper.StudentRowMapper;
import com.nesterov.university.model.Student;

@Component
public class StudentDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentDao.class);

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
		LOGGER.debug("Creating '{}'", student);
		final KeyHolder holder = new GeneratedKeyHolder();
		int affectedRows = jdbcTemplate.update(connection -> {
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
		if (affectedRows == 0) {
			String message = format("Student '%s' not created ", student);
			LOGGER.error(message);
			throw new NotCreateException(message);
		} else {
			LOGGER.trace("Successfully created '{}'", student);
		}
	}

	public Student get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting student by id = '{}'", id);
		Student student = new Student();
		try {
			student = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(student.toString());
			String message = format("Student with id '%s' not found", id);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Student with id '%s'", id);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Founded '{}'", student);
		return student;
	}

	public void delete(long id) throws NotExistException {
		LOGGER.debug("Deleting student by id = '{}'", id);
		int affectedRows = jdbcTemplate.update(DELETE, id);
		if (affectedRows == 0) {
			LOGGER.error("Student was not deleted");
			String message = format("Student with id = '%s' not exist ", id);
			throw new NotExistException(message);
		} else {
			LOGGER.trace("Deleted student with id = '{}'", id);
		}
	}

	public void update(Student student) throws NotCreateException {
		LOGGER.debug("Updating student '{}'", student);
		int affectedRows = jdbcTemplate.update(UPDATE, student.getFirstName(), student.getLastName(),
				student.getBithDate(), student.getAddress(), student.getEmail(), student.getPhone(),
				student.getGender().name(), student.getId());
		if (affectedRows == 0) {
			LOGGER.error("Student was not updated");
			String message = format("Student '%s' not updated", student);
			throw new NotCreateException(message);
		} else {
			LOGGER.trace("Updated '{}'", student);
		}
	}

	public List<Student> findAll() throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting all students");
		List<Student> students = null;
		try {
			students = jdbcTemplate.query(SELECT, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = "No Students";
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = "Unable to get students";
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all students");
		return students;
	}

	public List<Student> findByGroupId(long id) throws QueryNotExecuteException, EntityNotFoundException {
		LOGGER.debug("Getting adience by groupId = '{}'", id);
		List<Student> students = null;
		try {
			students = jdbcTemplate.query(SELECT_BY_GROUP, new Object[] { id }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Students with groupId = '%s' not found", id);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Students with groupId '%s'", id);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded students by groupId = '{}'", id);
		return students;
	}

	public Student findByEmail(String email) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting student by email = '{}'", email);
		Student student = null;
		try {
			student = jdbcTemplate.queryForObject(SELECT_BY_EMAIL, new Object[] { email }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Student with email = '%s' not found", email);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Student by email = '%s'", email);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded student by email = '{}'", email);
		return student;
	}

	public Student findByPhone(String phone) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting student by phone = '{}'", phone);
		Student student = null;
		try {
			student = jdbcTemplate.queryForObject(SELECT_BY_PHONE, new Object[] { phone }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Student with phone = '%s' not found", phone);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Student with phone = '%s'", phone);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded student by phone = '{}'", phone);
		return student;
	}

	public Student findByAddress(String address) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting student by address = '{}'", address);
		Student student = null;
		try {
			student = jdbcTemplate.queryForObject(SELECT_BY_ADDRESS, new Object[] { address }, studentRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Student with address = '%s' not found", address);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Student with address = '%s'", address);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded student by address = '{}'", address);
		return student;
	}
}
