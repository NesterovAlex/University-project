package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherRowMapper implements RowMapper<Teacher> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherRowMapper.class);

	private SubjectDao subjectDao;

	public TeacherRowMapper(SubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}

	@Override
	public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Teacher teacher = new Teacher();
		long id = resultSet.getLong("id");
		teacher.setId(id);
		teacher.setFirstName(resultSet.getString("first_name"));
		teacher.setLastName(resultSet.getString("last_name"));
		teacher.setBirthDate(resultSet.getObject("birth_date", LocalDate.class));
		teacher.setAddress(resultSet.getString("address"));
		teacher.setEmail(resultSet.getString("email"));
		teacher.setPhone(resultSet.getString("phone"));
		teacher.setGender(Gender.valueOf(resultSet.getString("gender")));
		try {
			teacher.setSubjects(subjectDao.findByTeacherId(id));
		} catch (QueryNotExecuteException | EntityNotFoundException e) {
			LOGGER.error(e.getMessage());
		}
		return teacher;
	}
}
