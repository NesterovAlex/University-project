package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.nesterov.university.model.Subject;

@Component
public class SubjectSimpleRowMapper implements RowMapper<Subject>{

	@Override
	public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
		Subject subject = new Subject();
		long id = rs.getLong("id");
		subject.setId(id);
		subject.setName(rs.getString("name"));
		return subject;
	}
}
