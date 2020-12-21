package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Subject;

@Component
public class SubjectRowMapper implements RowMapper<Subject> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubjectRowMapper.class);

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
		try {
			subject.setTeachers(teacherDao.findBySubjectId(id));
		} catch (EntityNotFoundException | QueryNotExecuteException e) {
			LOGGER.error(e.getMessage());
		}
		return subject;
	}
}
