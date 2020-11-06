package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.mapper.SubjectRowMapper;
import com.nesterov.university.model.Subject;

@Component
public class SubjectDao {

	private static final String SELECT_BY_TEACHER = "SELECT * FROM subjects LEFT JOIN teachers_subjects ON teachers_subjects.subject_id = subjects.id LEFT JOIN teachers ON teachers_subjects.teacher_id = teachers.id WHERE teacher_id = ?";
	private static final String INSERT_INTO_TEACHER_SUBJECT = "INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (?, ?)";
	private static final String SELECT_BY_ID = "SELECT *  FROM subjects WHERE id = ?";
	private static final String SELECT = "SELECT * FROM subjects";
	private static final String INSERT = "INSERT INTO subjects (name) values (?)";
	private static final String UPDATE = "UPDATE subjects SET name = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM subjects WHERE id = ?";

	private JdbcTemplate template;

	public SubjectDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(Subject subject) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, subject.getName());
			return statement;
		}, holder);
		long id = holder.getKey().longValue();
		subject.setId(id);
		subject.getTeachers().stream().forEach(t -> template.update(INSERT_INTO_TEACHER_SUBJECT, t.getId(), id));
	}

	public Subject getSubject(long id) {
		return template.queryForObject(SELECT_BY_ID, new Object[] { id }, new SubjectRowMapper(template));
	}

	public void delete(long id) {
		template.update(DELETE, id);
	}

	public void update(Subject subject) {
		template.update(UPDATE, subject.getName(), subject.getId());
	}

	public List<Subject> getAll() {
		return template.query(SELECT, (rs, rowNum) -> new Subject(rs.getLong("id"), rs.getString("name")));
	}

	public List<Subject> getAllByTeacher(long id) {
		return template.query(SELECT_BY_TEACHER, new Object[] { id },
				(rs, rowNum) -> new Subject(rs.getLong("id"), rs.getString("name")));
	}
}
