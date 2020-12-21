package com.nesterov.university.dao;

import static java.lang.String.format;

import java.sql.PreparedStatement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.dao.mapper.TeacherRowMapper;
import com.nesterov.university.dao.mapper.TeacherSimpleRowMapper;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDao.class);

	private static final String INSERT_INTO_TEACHERS_SUBJECTS = "INSERT INTO teachers_subjects SELECT ?, ? FROM DUAL WHERE NOT EXISTS (SELECT * FROM teachers_subjects WHERE teacher_id = ? AND subject_id = ?);";
	private static final String DELETE_FROM_TEACHERS_SUBJECTS = "DELETE FROM teachers_subjects WHERE subject_id = ? AND teacher_id = ?";
	private static final String SELECT_BY_SUBJECT = "SELECT * FROM teachers LEFT JOIN teachers_subjects ON teachers_subjects.teacher_id = teachers.id LEFT JOIN subjects ON teachers_subjects.subject_id = subjects.id WHERE subject_id = ?";
	private static final String SELECT_BY_ID = "SELECT *  FROM teachers WHERE id = ?";
	private static final String SELECT_BY_EMAIL = "SELECT *  FROM teachers WHERE email = ?";
	private static final String SELECT_BY_PHONE = "SELECT *  FROM teachers WHERE phone = ?";
	private static final String SELECT_BY_ADDRESS = "SELECT *  FROM teachers WHERE address = ?";
	private static final String INSERT = "INSERT INTO teachers (first_name, last_name, birth_date, address, email, phone, gender) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT = "SELECT *  FROM teachers";
	private static final String UPDATE = "UPDATE teachers SET first_name = ?, last_name = ?, birth_date = ?, address = ?, email = ?, phone = ?, gender = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM teachers WHERE id = ?";

	private TeacherSimpleRowMapper teacherSimpleRowMapper;
	private TeacherRowMapper teacherRowMapper;
	private JdbcTemplate jdbcTemplate;
	private SubjectDao subjectDao;

	public TeacherDao(JdbcTemplate template, @Lazy TeacherRowMapper teacherRowMapper,
			TeacherSimpleRowMapper teacherSimpleRowMapper, @Lazy SubjectDao subjectDao) {
		this.jdbcTemplate = template;
		this.teacherRowMapper = teacherRowMapper;
		this.teacherSimpleRowMapper = teacherSimpleRowMapper;
		this.subjectDao = subjectDao;
	}

	@Transactional
	public void create(Teacher teacher) throws NotCreateException {
		LOGGER.debug("Creating '{}'", teacher);
		final KeyHolder holder = new GeneratedKeyHolder();
		int affectedRows = jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, teacher.getFirstName());
			statement.setString(2, teacher.getLastName());
			statement.setObject(3, teacher.getBithDate());
			statement.setString(4, teacher.getAddress());
			statement.setString(5, teacher.getEmail());
			statement.setString(6, teacher.getPhone());
			statement.setString(7, teacher.getGender().name());
			return statement;
		}, holder);
		long id = holder.getKey().longValue();
		teacher.setId(id);
		if (affectedRows == 0) {
			String message = format("Teacher '%s' not created ", teacher);
			LOGGER.error(message);
			throw new NotCreateException(message);
		} else {
			teacher.getSubjects()
					.forEach(s -> jdbcTemplate.update(INSERT_INTO_TEACHERS_SUBJECTS, id, s.getId(), id, s.getId()));
			LOGGER.trace("Successfully created '{}'", teacher);
		}
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Teacher get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting teacher by id = '{}'", id);
		Teacher teacher = new Teacher();
		try {
			teacher = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, teacherRowMapper);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(teacher.toString());
			String message = format("Teacher with id '%s' not found", id);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			LOGGER.error(teacher.toString());
			String message = format("Unable to get Teacher with id '%s'", id);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Founded '{}'", teacher);
		return teacher;
	}

	@Transactional
	public void delete(long id) throws NotExistException {
		LOGGER.debug("Deleting teacher by id = '{}'", id);
		int affectedRows = jdbcTemplate.update(DELETE, id);
		if (affectedRows == 0) {
			LOGGER.error("Student was not deleted");
			String message = format("Teacher with id = '%s' not exist ", id);
			throw new NotExistException(message);
		} else {
			LOGGER.trace("Deleted teacher with id = '{}'", id);
		}
	}

	@Transactional
	public void update(Teacher teacher) throws NotCreateException, QueryNotExecuteException, EntityNotFoundException {
		LOGGER.debug("Updating teacher '{}'", teacher);
		int affectedRows = jdbcTemplate.update(UPDATE, teacher.getFirstName(), teacher.getLastName(),
				teacher.getBithDate(), teacher.getAddress(), teacher.getEmail(), teacher.getPhone(),
				teacher.getGender().name(), teacher.getId());
		if (affectedRows == 0) {
			LOGGER.error("Teacher was not updated");
			String message = format("Teacher '%s' not updated", teacher);
			throw new NotCreateException(message);
		} else {
			List<Subject> subjects = subjectDao.findByTeacherId(teacher.getId());
			subjects.stream().filter(s -> !teacher.getSubjects().contains(s))
					.forEach(s -> jdbcTemplate.update(DELETE_FROM_TEACHERS_SUBJECTS, s.getId(), teacher.getId()));
			teacher.getSubjects().stream().filter(s -> !subjects.contains(s)).forEach(s -> jdbcTemplate
					.update(INSERT_INTO_TEACHERS_SUBJECTS, teacher.getId(), s.getId(), teacher.getId(), s.getId()));
			LOGGER.trace("Updated '{}'", teacher);
		}
	}

	public List<Teacher> findAll() throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting all teachers");
		List<Teacher> teachers = null;
		try {
			teachers = jdbcTemplate.query(SELECT, teacherRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = "No Teachers";
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = "Unable to get teachers";
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all teachers");
		return teachers;
	}

	public List<Teacher> findBySubjectId(long id) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting teachers by subjectId = '{}'", id);
		List<Teacher> teachers = null;
		try {
			teachers = jdbcTemplate.query(SELECT_BY_SUBJECT, new Object[] { id }, teacherSimpleRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Teachers with subjectId = '%s' not found", id);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Teachers with subjectId '%s'", id);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded Teachers by subjectId = '{}'", id);
		return teachers;
	}

	public Teacher findByEmail(String email) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting teacher by email = '{}'", email);
		Teacher teacher = null;
		try {
			teacher = jdbcTemplate.queryForObject(SELECT_BY_EMAIL, new Object[] { email }, teacherRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Teacher with email = '%s' not found", email);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Teacher by email = '%s'", email);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded teacher by email = '{}'", email);
		return teacher;
	}

	public Teacher findByPhone(String phone) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting teacher by phone = '{}'", phone);
		Teacher teacher = null;
		try {
			teacher = jdbcTemplate.queryForObject(SELECT_BY_PHONE, new Object[] { phone }, teacherRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Teacher with phone = '%s' not found", phone);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get teacher with phone = '%s'", phone);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded teacher by phone = '{}'", phone);
		return teacher;
	}

	public Teacher findByAddress(String address) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting teacher by address = '{}'", address);
		Teacher teacher = null;
		try {
			teacher = jdbcTemplate.queryForObject(SELECT_BY_ADDRESS, new Object[] { address }, teacherRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Teacher with address = '%s' not found", address);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Teacher with address = '%s'", address);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded teacher by address = '{}'", address);
		return teacher;
	}
}