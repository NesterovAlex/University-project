package com.nesterov.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.model.Subject;

@Component
public class SubjectRowMapper implements RowMapper<Subject> {

	private TeacherDao dao;

	public SubjectRowMapper() {}
	
	public SubjectRowMapper(JdbcTemplate template) {
		dao = new TeacherDao(template);
	}

	@Override
	public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
		Subject subject = new Subject();
		long id = rs.getLong("id");
		subject.setId(id);
		subject.setName(rs.getString("name"));
		subject.setTeachers(dao.getAllBySubject(id));
		return subject;
	}

}
