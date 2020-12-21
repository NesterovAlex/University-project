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
import com.nesterov.university.dao.mapper.SubjectRowMapper;
import com.nesterov.university.dao.mapper.SubjectSimpleRowMapper;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@Component
public class SubjectDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubjectDao.class);

	private static final String INSERT_INTO_TEACHERS_SUBJECTS = "INSERT INTO teachers_subjects SELECT ?, ? FROM DUAL WHERE NOT EXISTS (SELECT * FROM teachers_subjects WHERE teacher_id = ? AND subject_id = ?);";
	private static final String DELETE_FROM_TEACHERS_SUBJECTS = "DELETE FROM teachers_subjects WHERE teacher_id = ? AND subject_id = ?";
	private static final String SELECT_BY_TEACHER = "SELECT * FROM subjects LEFT JOIN teachers_subjects ON teachers_subjects.subject_id = subjects.id LEFT JOIN teachers ON teachers_subjects.teacher_id = teachers.id WHERE teacher_id = ?";
	private static final String SELECT_BY_NAME = "SELECT *  FROM subjects WHERE name = ?";
	private static final String SELECT_BY_ID = "SELECT *  FROM subjects WHERE id = ?";
	private static final String SELECT = "SELECT * FROM subjects";
	private static final String INSERT = "INSERT INTO subjects (name) values (?)";
	private static final String UPDATE = "UPDATE subjects SET name = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM subjects WHERE id = ?";

	private JdbcTemplate jdbcTemplate;
	private SubjectRowMapper subjectRowMapper;
	private SubjectSimpleRowMapper subjectSimpleRowMapper;
	private TeacherDao teacherDao;

	public SubjectDao(JdbcTemplate template, SubjectRowMapper subjectRowMapper,
			SubjectSimpleRowMapper subjectSimpleRowMapper, TeacherDao teacherDao) {
		this.jdbcTemplate = template;
		this.subjectRowMapper = subjectRowMapper;
		this.subjectSimpleRowMapper = subjectSimpleRowMapper;
		this.teacherDao = teacherDao;
	}

	public void create(Subject subject) throws NotCreateException {
		LOGGER.debug("Creating '{}'", subject);
		final KeyHolder holder = new GeneratedKeyHolder();
		int affectedRows = jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, subject.getName());
			return statement;
		}, holder);
		long id = holder.getKey().longValue();
		subject.setId(id);
		if (affectedRows == 0) {
			String message = format("Subject '%s' not created ", subject);
			LOGGER.error(message);
			throw new NotCreateException(message);
		} else {
			subject.getTeachers().stream()
					.forEach(t -> jdbcTemplate.update(INSERT_INTO_TEACHERS_SUBJECTS, t.getId(), id, t.getId(), id));
			LOGGER.trace("Successfully created '{}'", subject);
		}
	}

	public Subject get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting subject by id = '{}'", id);
		Subject subject = new Subject();
		try {
			subject = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, subjectRowMapper);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(subject.toString());
			String message = format("Subject with id '%s' not found", id);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Subject with id '%s'", id);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Founded '{}'", subject);
		return subject;
	}

	public void delete(long id) throws NotExistException {
		LOGGER.debug("Deleting subject by id = '{}'", id);
		int affectedRows = jdbcTemplate.update(DELETE, id);
		if (affectedRows == 0) {
			LOGGER.error("Subject was not deleted");
			String message = format("Subject with id = '%s' not exist ", id);
			throw new NotExistException(message);
		} else {
			LOGGER.trace("Deleted subject with id = '{}'", id);
		}
	}

	public void update(Subject subject) throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		LOGGER.debug("Updating subject '{}'", subject);
		int affectedRows = jdbcTemplate.update(UPDATE, subject.getName(), subject.getId());
		if (affectedRows == 0) {
			LOGGER.error("Subject was not updated");
			String message = format("Subject '%s' not updated", subject);
			throw new NotCreateException(message);
		} else {
			List<Teacher> teachers = teacherDao.findBySubjectId(subject.getId());
			teachers.stream().filter(t -> !subject.getTeachers().contains(t))
					.forEach(t -> jdbcTemplate.update(DELETE_FROM_TEACHERS_SUBJECTS, t.getId(), subject.getId()));
			subject.getTeachers().stream().filter(t -> !teachers.contains(t)).forEach(s -> jdbcTemplate
					.update(INSERT_INTO_TEACHERS_SUBJECTS, s.getId(), subject.getId(), s.getId(), subject.getId()));
			LOGGER.trace("Updated '{}'", subject);
		}
	}

	public List<Subject> findAll() throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting all subjects");
		List<Subject> subjects = null;
		try {
			subjects = jdbcTemplate.query(SELECT, subjectRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = "No Subjects";
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = "Unable to get subjects";
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all subjects");
		return subjects;
	}

	public List<Subject> findByTeacherId(long id) throws QueryNotExecuteException, EntityNotFoundException {
		LOGGER.debug("Getting subjects by teacherId = '{}'", id);
		List<Subject> subjects = null;
		try {
			subjects = jdbcTemplate.query(SELECT_BY_TEACHER, new Object[] { id }, subjectSimpleRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Subjects with teacherId = '%s' not found", id);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get subjects by teacherId '%s'", id);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded subjects by teacherId = '{}'", id);
		return subjects;
	}

	public Subject findByName(String name) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting subject by name = '{}'", name);
		Subject subject = null;
		try {
			subject = jdbcTemplate.queryForObject(SELECT_BY_NAME, new Object[] { name }, subjectRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Subject with name = '%s' not found", name);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get subject by name = '%s'", name);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded subject by name = '{}'", name);
		return subject;
	}
}
