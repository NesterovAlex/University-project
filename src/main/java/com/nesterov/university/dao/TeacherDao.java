package com.nesterov.university.dao;

import static java.util.Optional.of;
import static java.util.Optional.empty;

import java.sql.PreparedStatement;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.nesterov.university.dao.mapper.TeacherRowMapper;
import com.nesterov.university.dao.mapper.TeacherSimpleRowMapper;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherDao {

	private static final Logger log = LoggerFactory.getLogger(TeacherDao.class);

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
	public void create(Teacher teacher) {
		log.debug("Create {}", teacher);
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
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
		teacher.getSubjects()
				.forEach(s -> jdbcTemplate.update(INSERT_INTO_TEACHERS_SUBJECTS, id, s.getId(), id, s.getId()));
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Optional<Teacher> get(long id) {
		log.debug("Get teacher by id={}", id);
		try {
			return of(jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, teacherRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}

	@Transactional
	public void delete(long id) {
		log.debug("Delete teacher by id={}", id);
		jdbcTemplate.update(DELETE, id);
	}

	@Transactional
	public void update(Teacher teacher) {
		log.debug("Update teacher {}", teacher);
		jdbcTemplate.update(UPDATE, teacher.getFirstName(), teacher.getLastName(), teacher.getBithDate(),
				teacher.getAddress(), teacher.getEmail(), teacher.getPhone(), teacher.getGender().name(),
				teacher.getId());
		List<Subject> subjects = subjectDao.findByTeacherId(teacher.getId());
		subjects.stream().filter(s -> !teacher.getSubjects().contains(s))
				.forEach(s -> jdbcTemplate.update(DELETE_FROM_TEACHERS_SUBJECTS, s.getId(), teacher.getId()));
		teacher.getSubjects().stream().filter(s -> !subjects.contains(s)).forEach(s -> jdbcTemplate
				.update(INSERT_INTO_TEACHERS_SUBJECTS, teacher.getId(), s.getId(), teacher.getId(), s.getId()));
	}

	public List<Teacher> findAll() {
		log.debug("Find all teachers");
		return jdbcTemplate.query(SELECT, teacherRowMapper);
	}

	public List<Teacher> findBySubjectId(long id) {
		log.debug("Find teachers by subjectId={}", id);
		return jdbcTemplate.query(SELECT_BY_SUBJECT, new Object[] { id }, teacherSimpleRowMapper);
	}

	public Optional<Teacher> findByEmail(String email) {
		log.debug("Find teacher by email={}", email);
		try {
			return of(jdbcTemplate.queryForObject(SELECT_BY_EMAIL, new Object[] { email }, teacherRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}

	public Optional<Teacher> findByPhone(String phone) {
		log.debug("Find teacher by phone={}", phone);
		try {
			return of(jdbcTemplate.queryForObject(SELECT_BY_PHONE, new Object[] { phone }, teacherRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}

	public Optional<Teacher> findByAddress(String address) {
		log.debug("Find teacher by address={}", address);
		try {
			return of(jdbcTemplate.queryForObject(SELECT_BY_ADDRESS, new Object[] { address }, teacherRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}
}