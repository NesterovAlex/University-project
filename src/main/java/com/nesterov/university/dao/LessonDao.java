package com.nesterov.university.dao;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.nesterov.university.dao.mapper.LessonRowMapper;
import com.nesterov.university.model.Group;
import com.nesterov.university.model.Lesson;

@Component
public class LessonDao {

	private static final Logger log = LoggerFactory.getLogger(LessonDao.class);

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
	public void create(Lesson lesson) {
		log.debug("Create {}", lesson);
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
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
		lesson.getGroups().forEach(g -> jdbcTemplate.update(INSERT_INTO_LESSONS_GROUPS, id, g.getId(), id, g.getId()));
	}

	public Optional<Lesson> get(long id) {
		log.debug("Get lesson by id={}", id);
		try {
			return of(jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, lessonRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}

	@Transactional
	public void delete(long id) {
		log.debug("Delete lesson by id={}", id);
		jdbcTemplate.update(DELETE, id);
	}

	@Transactional
	public void update(Lesson lesson) {
		log.debug("Update lesson by id={}", lesson);
		jdbcTemplate.update(UPDATE, lesson.getAudience().getId(), lesson.getSubject().getId(),
				lesson.getTeacher().getId(), lesson.getTime().getId(), lesson.getDate(), lesson.getId());
		List<Group> groups = groupDao.findByLessonId(lesson.getId());
		groups.stream().filter(g -> !lesson.getGroups().contains(g))
				.forEach(g -> jdbcTemplate.update(DELETE_FROM_LESSONS_GROUPS, lesson.getId(), g.getId()));
		lesson.getGroups().stream().filter(g -> !groups.contains(g)).forEach(g -> jdbcTemplate
				.update(INSERT_INTO_LESSONS_GROUPS, lesson.getId(), g.getId(), lesson.getId(), g.getId()));
	}

	@Transactional
	public List<Lesson> findAll() {
		log.debug("Find all lessons");
		return jdbcTemplate.query(SELECT, lessonRowMapper);
	}

	public List<Lesson> findByDateAndAudience(LocalDate date, long lessonTimeId, long audienceId) {
		log.debug("Find audiences by date = {}, lessonTimeId={}, audienceId={}", date, lessonTimeId, audienceId);
		return jdbcTemplate.query(SELECT_BY_DATE_AUDIENCE, new Object[] { date, lessonTimeId, audienceId },
				lessonRowMapper);
	}

	public List<Lesson> findByDateAndGroups(LocalDate date, long lessonTimeId) {
		log.debug("Find lessons by date={}, lessonTimeId={} and groups", date, lessonTimeId);
		return jdbcTemplate.query(SELECT_BY_DATE_GROUPS, new Object[] { date, lessonTimeId }, lessonRowMapper);
	}

	public List<Lesson> findByDateAndTeacher(LocalDate date, long lessonTimeId, long teacherId) {
		log.debug("Find lessons by date={}, lessonTimeId={}, teacherId={}", date, lessonTimeId, teacherId);
		return jdbcTemplate.query(SELECT_BY_DATE_TEACHER, new Object[] { date, lessonTimeId, teacherId },
				lessonRowMapper);
	}
}
