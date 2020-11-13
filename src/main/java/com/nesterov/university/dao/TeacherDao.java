package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nesterov.university.mapper.TeacherRowMapper;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherDao {

	private static final String INSERT_INTO_COPY_TEACHERS_SUBJECTS = "INSERT INTO copy_teachers_subjects SELECT ?, ? FROM DUAL WHERE NOT EXISTS (SELECT * FROM copy_teachers_subjects WHERE teacher_id = ? AND subject_id = ?);";
	private static final String CREATE_COPY_TEACHERS_SUBJECTS = "DROP TABLE IF EXISTS copy_teachers_subjects; CREATE TABLE copy_teachers_subjects as select * from teachers_subjects where 1=2; INSERT INTO copy_teachers_subjects SELECT * FROM teachers_subjects; ";
	private static final String DELETE_SUPERFLUOUS_SUBJECT = "DELETE FROM teachers_subjects AS t1 WHERE t1.subject_id NOT IN (SELECT t2.subject_id FROM copy_teachers_subjects AS t2)";
	private static final String INSERT_INTO_TEACHERS_SUBJECTS = "INSERT INTO teachers_subjects SELECT ?, ? FROM DUAL WHERE NOT EXISTS (SELECT * FROM teachers_subjects WHERE teacher_id = ? AND subject_id = ?);";
	private static final String DELETE_FROM_TEACHERS_SUBJECTS = "DELETE FROM teachers_subjects WHERE teacher_id = ?";
	private static final String SELECT_BY_SUBJECT = "SELECT * FROM teachers LEFT JOIN teachers_subjects ON teachers_subjects.teacher_id = teachers.id LEFT JOIN subjects ON teachers_subjects.subject_id = subjects.id WHERE subject_id = ?";
	private static final String SELECT_BY_ID = "SELECT *  FROM teachers WHERE id = ?";
	private static final String INSERT = "INSERT INTO teachers (first_name, last_name, birth_date, address, email, phone, gender) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT = "SELECT *  FROM teachers";
	private static final String UPDATE = "UPDATE teachers SET first_name = ?, last_name = ?, birth_date = ?, address = ?, email = ?, phone = ?, gender = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM teachers WHERE id = ?";

	@Autowired
	private TeacherRowMapper teacherRowMapper;
	private JdbcTemplate template;

	public TeacherDao(JdbcTemplate template) {
		this.template = template;
	}

	@Transactional
	public void create(Teacher teacher) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
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
		teacher.getSubjects().forEach(s -> template.update(INSERT_INTO_TEACHERS_SUBJECTS, id, s.getId(), id, s.getId()));
	}

	public Teacher get(long id) {
		return template.queryForObject(SELECT_BY_ID, new Object[] { id }, teacherRowMapper);
	}

	@Transactional
	public void delete(long id) {
		template.update(DELETE, id);
		template.update(DELETE_FROM_TEACHERS_SUBJECTS, id);
	}

	@Transactional
	public void update(Teacher teacher) {
		template.execute(CREATE_COPY_TEACHERS_SUBJECTS);
		template.update(UPDATE, teacher.getFirstName(), teacher.getLastName(), teacher.getBithDate(),
				teacher.getAddress(), teacher.getEmail(), teacher.getPhone(), teacher.getGender().name(),
				teacher.getId());
		teacher.getSubjects().forEach(s -> template.update(INSERT_INTO_TEACHERS_SUBJECTS, teacher.getId(), s.getId(),
				teacher.getId(), s.getId()));
		teacher.getSubjects().forEach(s -> template.update(INSERT_INTO_COPY_TEACHERS_SUBJECTS, teacher.getId(), s.getId(),
				teacher.getId(), s.getId()));
		template.execute(DELETE_SUPERFLUOUS_SUBJECT);
	}

	public List<Teacher> getAll() {
		return template.query(SELECT,teacherRowMapper);
	}

	public List<Teacher> getAllBySubject(long id) {
		return template.query(SELECT_BY_SUBJECT, new Object[] { id }, teacherRowMapper);
	}
}