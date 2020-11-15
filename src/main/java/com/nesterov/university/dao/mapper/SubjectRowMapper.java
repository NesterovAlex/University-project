package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.model.Subject;

@Component
public class SubjectRowMapper implements RowMapper<Subject> {

	private TeacherDao teacherDao;

	public SubjectRowMapper(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}
	
	@Override
	public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
		Subject subject = new Subject();
		long id = rs.getLong("id");
		subject.setId(id);
		subject.setName(rs.getString("name"));
		subject.setTeachers(teacherDao.findBySubjectId(id));
		return subject;
	}
}
