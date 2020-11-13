package com.nesterov.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.model.Subject;

@Component
public class SubjectRowMapper implements RowMapper<Subject> {

	@Autowired
	private TeacherDao dao;

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
