package com.nesterov.university.dao;

import static java.lang.String.format;

import java.sql.PreparedStatement;
import java.time.LocalDate;
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
import org.springframework.transaction.annotation.Transactional;

import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.dao.mapper.LessonRowMapper;
import com.nesterov.university.model.Group;
import com.nesterov.university.model.Lesson;

@Component
public class LessonDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(LessonDao.class);

	private static final String SELECT_BY_DATE_TEACHER = "SELECT * FROM lessons WHERE lesson_date = ? AND lesson_time_id = ? AND teacher_id = ?";
	private static final String SELECT_BY_DATE_GROUPS = "SELECT * FROM lessons INNER JOIN lessons_groups ON lessons.id = lessons_groups.lesson_id WHERE lessons.lesson_date = ? AND lessons.lesson_time_id = ?";
	private static final String SELECT_BY_DATE_AUDIENCE = "SELECT * FROM lessons WHERE lesson_date = ? AND lesson_time_id = ? AND audience_id = ?";
	private static final String DELETE_FROM_LESSONS_GROUPS = "DELETE FROM lessons_groups WHERE lesson_id = ? AND group_id = ?";
	private static final String INSERT_INTO_LESSONS_GROUPS = "INSERT INTO lessons_groups SELECT ?, ? FROM DUAL WHERE NOT EXISTS (SELECT FROM lessons_groups WHERE lesson_id = ? AND group_id = ?);";
	private static final String SELECT_BY_ID = "SELECT * FROM lessons WHERE id = ?";
	private static final String SELECT = "SELECT * FROM lessons";
	private static final String INSERT = "INSERT INTO lessons (audience_id, subject_id, teacher_id, lesson_time_id, lesson_date) values (?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE lessons SET audience_id = ?, subject_id = ?, teacher_id = ?, lesson_time_id = ?, lesson_date = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM lessons WHERE id = ?";

	private LessonRowMapper lessonRowMapper;
	private JdbcTemplate jdbcTemplate;
	private GroupDao groupDao;

	public LessonDao(JdbcTemplate template, @Lazy LessonRowMapper lessonRowMapper, GroupDao groupDao) {
		this.jdbcTemplate = template;
		this.lessonRowMapper = lessonRowMapper;
		this.groupDao = groupDao;
	}

	@Transactional
	public void create(Lesson lesson) throws NotCreateException {
		LOGGER.debug("Creating '{}'", lesson);
		final KeyHolder holder = new GeneratedKeyHolder();
		int affectedRows = jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setLong(1, lesson.getAudience().getId());
			statement.setLong(2, lesson.getSubject().getId());
			statement.setLong(3, lesson.getTeacher().getId());
			statement.setLong(4, lesson.getTime().getId());
			statement.setObject(5, lesson.getDate());
			return statement;
		}, holder);
		long id = holder.getKey().longValue();
		lesson.setId(id);
		if (affectedRows == 0) {
			LOGGER.error("Lesson not created");
			String message = format("Lesson '%s' not created ", lesson);
			throw new NotCreateException(message);
		} else {
			lesson.getGroups()
					.forEach(g -> jdbcTemplate.update(INSERT_INTO_LESSONS_GROUPS, id, g.getId(), id, g.getId()));
			LOGGER.trace("Successfully created '{}'", lesson);
		}
	}

	public Lesson get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting lesson by id = '{}'", id);
		Lesson lesson = new Lesson();
		try {
			lesson = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, lessonRowMapper);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(lesson.toString());
			String message = format("Lesson with id '%s' not found", id);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			LOGGER.error(lesson.toString());
			String message = format("Unable to get Lesson with id '%s'", id);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Founded '{}'", lesson);
		return lesson;
	}

	@Transactional
	public void delete(long id) throws NotExistException {
		LOGGER.debug("Deleting lesson by id = '{}'", id);
		int affectedRows = jdbcTemplate.update(DELETE, id);
		if (affectedRows == 0) {
			LOGGER.error("Lesson was not deleted");
			String message = format("Lesson with id = '%s' not exist ", id);
			throw new NotExistException(message);
		} else {
			LOGGER.trace("Deleted Lesson with id = '{}'", id);
		}
	}

	@Transactional
	public void update(Lesson lesson) throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		LOGGER.debug("Updating lesson by id = '{}'", lesson);
		int affectedRows = jdbcTemplate.update(UPDATE, lesson.getAudience().getId(), lesson.getSubject().getId(),
				lesson.getTeacher().getId(), lesson.getTime().getId(), lesson.getDate(), lesson.getId());
		if (affectedRows == 0) {
			LOGGER.error("Lesson was not updated");
			String message = format("Audience '%s' not updated", lesson);
			throw new NotCreateException(message);
		} else {
			List<Group> groups = groupDao.findByLessonId(lesson.getId());
			groups.stream().filter(g -> !lesson.getGroups().contains(g))
					.forEach(g -> jdbcTemplate.update(DELETE_FROM_LESSONS_GROUPS, lesson.getId(), g.getId()));
			lesson.getGroups().stream().filter(g -> !groups.contains(g)).forEach(g -> jdbcTemplate
					.update(INSERT_INTO_LESSONS_GROUPS, lesson.getId(), g.getId(), lesson.getId(), g.getId()));
			LOGGER.trace("Updated '{}'", lesson);
		}
	}

	@Transactional
	public List<Lesson> findAll() throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting all groups");
		List<Lesson> lessons = null;
		try {
			lessons = jdbcTemplate.query(SELECT, lessonRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = "No lessons";
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = "Unable to get Lessons";
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all lessons");
		return lessons;
	}

	public List<Lesson> findByDateAndAudience(LocalDate date, long lessonTimeId, long audienceId)
			throws EntityNotFoundException, QueryNotExecuteException {
		List<Lesson> lessons = null;
		LOGGER.debug("Getting audiences by date = '{}', lessonTimeId = '{}', audienceId = '{}'", date, lessonTimeId,
				audienceId);
		try {
			lessons = jdbcTemplate.query(SELECT_BY_DATE_AUDIENCE, new Object[] { date, lessonTimeId, audienceId },
					lessonRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Lessons with date = '%s', lessonTimeId = '%s', audienceId = '%s' not found", date,
					lessonTimeId, audienceId);
			LOGGER.error(message, e);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Lessons date = '%s', lessonTimeId = '%s', audienceId = '%s'", date,
					lessonTimeId, audienceId);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all lessons by date and audience");
		return lessons;

	}

	public List<Lesson> findByDateAndGroups(LocalDate date, long lessonTimeId)
			throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting lessons by date = '{}', lessonTimeId = '{}' and groups", date, lessonTimeId);
		List<Lesson> lessons = null;
		try {
			lessons = jdbcTemplate.query(SELECT_BY_DATE_GROUPS, new Object[] { date, lessonTimeId }, lessonRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Lessons with date = '%s', lessonTimeId = '%s' and groups not found", date,
					lessonTimeId);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Lessons date = '%s', lessonTimeId = '%s' and groups", date,
					lessonTimeId);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all lessons by date and groups");
		return lessons;
	}

	public List<Lesson> findByDateAndTeacher(LocalDate date, long lessonTimeId, long teacherId)
			throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting lessons by date = '{}', lessonTimeId = '{}', teacherId = '{}'", date, lessonTimeId,
				teacherId);
		List<Lesson> lessons = null;
		try {
			lessons = jdbcTemplate.query(SELECT_BY_DATE_TEACHER, new Object[] { date, lessonTimeId, teacherId },
					lessonRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Lessons with date = '%s', lessonTimeId = '%s', teacherId = '%s' not found", date,
					lessonTimeId, teacherId);
			LOGGER.error(message, e);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = format("Unable to get Lessons date = '%s', lessonTimeId = '%s', teacherId = '%s'", date,
					lessonTimeId, teacherId);
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all lessons by date and teacher");
		return lessons;
	}
}
