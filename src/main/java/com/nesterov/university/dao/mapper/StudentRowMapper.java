package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Student;

@Component
public class StudentRowMapper implements RowMapper<Student> {
	
	@Override
	public Student mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Student student = new Student();
		student.setId(resultSet.getLong("id"));
		student.setGroupId(resultSet.getLong("group_id"));
		student.setFirstName(resultSet.getString("first_name"));
		student.setLastName(resultSet.getString("last_name"));
		student.setBirthDate(resultSet.getObject("birth_date", LocalDate.class));
		student.setAddress(resultSet.getString("address"));
		student.setEmail(resultSet.getString("email"));
		student.setPhone(resultSet.getString("phone"));
		student.setGender(Gender.valueOf(resultSet.getString("gender")));
		student.setFaculty(resultSet.getString("faculty"));
		student.setCourse(resultSet.getString("course"));
		return student;
	}
}
