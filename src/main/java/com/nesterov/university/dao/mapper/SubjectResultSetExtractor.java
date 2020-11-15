package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.ResultSetExtractor;
import com.nesterov.university.model.Subject;

public class SubjectResultSetExtractor implements ResultSetExtractor<List<Subject>>{

	@Override
	public List<Subject> extractData(ResultSet rs) throws SQLException{
		List<Subject> subjects = new ArrayList<>();
		while(rs.next()) {
			Subject subject = new Subject();
			subject.setId(rs.getLong("id"));
			subject.setName(rs.getString("name"));
		}
		return subjects;
	}

}
