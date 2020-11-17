package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.nesterov.university.dao.mapper.SubjectSimpleRowMapper;
import com.nesterov.university.dao.mapper.TeacherRowMapper;
import com.nesterov.university.dao.mapper.TeacherSimpleRowMapper;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherDao {

	private static final String SELECT_ALL_SUBJECTS_BY_TEACHER = "SELECT * FROM subjects LEFT JOIN teachers_subjects ON teachers_subjects.subject_id = subjects.id WHERE teacher_id = ?";
	private static final String INSERT_INTO_TEACHERS_SUBJECTS = "INSERT INTO teachers_subjects SELECT ?, ? FROM DUAL WHERE NOT EXISTS (SELECT * FROM teachers_subjects WHERE teacher_id = ? AND subject_id = ?);";
	private static final String DELETE_FROM_TEACHERS_SUBJECTS = "DELETE FROM teachers_subjects WHERE subject_id = ? AND teacher_id = ?";
	private static final String SELECT_BY_SUBJECT = "SELECT * FROM teachers LEFT JOIN teachers_subjects ON teachers_subjects.teacher_id = teachers.id LEFT JOIN subjects ON teachers_subjects.subject_id = subjects.id WHERE subject_id = ?";
	private static final String SELECT_BY_ID = "SELECT *  FROM teachers WHERE id = ?";
	private static final String INSERT = "INSERT INTO teachers (first_name, last_name, birth_date, address, email, phone, gender) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT = "SELECT *  FROM teachers";
	private static final String UPDATE = "UPDATE teachers SET first_name = ?, last_name = ?, birth_date = ?, address = ?, email = ?, phone = ?, gender = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM teachers WHERE id = ?";
	private TeacherSimpleRowMapper teacherSimpleRowMapper;
	private SubjectSimpleRowMapper subjectSimpleRowMapper;
	private TeacherRowMapper teacherRowMapper;
	private JdbcTemplate jdbcTemplate;

	public TeacherDao(JdbcTemplate template, @Lazy TeacherRowMapper teacherRowMapper,
			TeacherSimpleRowMapper teacherSimpleRowMapper, SubjectSimpleRowMapper subjectSimpleRowMapper) {
		this.jdbcTemplate = template;
		this.teacherRowMapper = teacherRowMapper;
		this.teacherSimpleRowMapper = teacherSimpleRowMapper;
		this.subjectSimpleRowMapper = subjectSimpleRowMapper;
	}

	@Transactional
	public void create(Teacher teacher) {
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
	public Teacher get(long id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, teacherRowMapper);
	}

	@Transactional
	public void delete(long id) {
		jdbcTemplate.update(DELETE, id);
	}

	@Transactional
	public void update(Teacher teacher) {
		jdbcTemplate.update(UPDATE, teacher.getFirstName(), teacher.getLastName(), teacher.getBithDate(),
				teacher.getAddress(), teacher.getEmail(), teacher.getPhone(), teacher.getGender().name(),
				teacher.getId());
		List<Subject> subjects = jdbcTemplate.query(SELECT_ALL_SUBJECTS_BY_TEACHER, subjectSimpleRowMapper,
				teacher.getId());
		subjects.removeAll(teacher.getSubjects());
		subjects.forEach(s -> jdbcTemplate.update(DELETE_FROM_TEACHERS_SUBJECTS, s.getId(), teacher.getId()));
		teacher.getSubjects().forEach(s -> jdbcTemplate.update(INSERT_INTO_TEACHERS_SUBJECTS, teacher.getId(),
				s.getId(), teacher.getId(), s.getId()));
	}

	public List<Teacher> getAll() {
		return jdbcTemplate.query(SELECT, teacherRowMapper);
	}

	public List<Teacher> findBySubjectId(long id) {
		return jdbcTemplate.query(SELECT_BY_SUBJECT, new Object[] { id }, teacherSimpleRowMapper);
	}
}