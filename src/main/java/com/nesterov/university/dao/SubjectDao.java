package com.nesterov.university.dao;

import static java.util.Optional.of;
import static java.util.Optional.empty;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.mapper.SubjectRowMapper;
import com.nesterov.university.dao.mapper.SubjectSimpleRowMapper;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@Component
public class SubjectDao {

	private static final Logger log = LoggerFactory.getLogger(SubjectDao.class);

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

	public void create(Subject subject) {
		log.debug("Create {}", subject);
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, subject.getName());
			return statement;
		}, holder);
		long id = holder.getKey().longValue();
		subject.setId(id);
		subject.getTeachers().stream()
				.forEach(t -> jdbcTemplate.update(INSERT_INTO_TEACHERS_SUBJECTS, t.getId(), id, t.getId(), id));
	}

	public Optional<Subject> get(long id) {
		log.debug("Get subject by id={}", id);
		try {
			return of(jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, subjectRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}

	public void delete(long id) {
		log.debug("Delete subject by id={}", id);
		jdbcTemplate.update(DELETE, id);
	}

	public void update(Subject subject) {
		log.debug("Update subject {}", subject);
		jdbcTemplate.update(UPDATE, subject.getName(), subject.getId());
		List<Teacher> teachers = teacherDao.findBySubjectId(subject.getId());
		teachers.stream().filter(t -> !subject.getTeachers().contains(t))
				.forEach(t -> jdbcTemplate.update(DELETE_FROM_TEACHERS_SUBJECTS, t.getId(), subject.getId()));
		subject.getTeachers().stream().filter(t -> !teachers.contains(t)).forEach(s -> jdbcTemplate
				.update(INSERT_INTO_TEACHERS_SUBJECTS, s.getId(), subject.getId(), s.getId(), subject.getId()));
	}

	public List<Subject> findAll() {
		log.debug("Find all subjects");
		return jdbcTemplate.query(SELECT, subjectRowMapper);
	}

	public List<Subject> findByTeacherId(long id) {
		log.debug("Find subjects by teacherId={}", id);
		return jdbcTemplate.query(SELECT_BY_TEACHER, new Object[] { id }, subjectSimpleRowMapper);
	}

	public Optional<Subject> findByName(String name) {
		log.debug("Find subject by name={}", name);
		try {
			return of(jdbcTemplate.queryForObject(SELECT_BY_NAME, new Object[] { name }, subjectRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}
}
