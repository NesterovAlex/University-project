package com.nesterov.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherRowMapper implements RowMapper<Teacher> {

	@Autowired
	private SubjectDao subjectDaoao;
	
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
		teacher.setSubjects(subjectDaoao.getAllByTeacher(id));
		return teacher;
	}

}
