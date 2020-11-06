package com.nesterov.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Teacher;

public class TeacherRowMapper implements RowMapper<Teacher> {

	private JdbcTemplate template;
	private SubjectDao dao;

	public TeacherRowMapper(JdbcTemplate template) {
		this.template = template;
		dao = new SubjectDao(template);
	}

	@Override
	public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Teacher teacher = new Teacher();
		long id = resultSet.getLong("id");
		teacher.setId(id);
		teacher.setFirstName(resultSet.getString("first_name"));
		teacher.setLastName(resultSet.getString("last_name"));
		teacher.setBirthDate(resultSet.getDate("birth_date"));
		teacher.setAddress(resultSet.getString("address"));
		teacher.setEmail(resultSet.getString("email"));
		teacher.setPhone(resultSet.getString("phone"));
		teacher.setGender(Gender.valueOf(resultSet.getString("gender")));
		teacher.setSubjects(dao.getAllByTeacher(id));
		return teacher;
	}

}
