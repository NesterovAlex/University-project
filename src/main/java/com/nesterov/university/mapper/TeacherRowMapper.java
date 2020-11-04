package com.nesterov.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Teacher;

public class TeacherRowMapper implements RowMapper<Teacher> {

	@Override
	public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Teacher teacher = new Teacher();
		teacher.setId(resultSet.getLong("id"));
		teacher.setFirstName(resultSet.getString("first_name"));
		teacher.setLastName(resultSet.getString("last_name"));
		teacher.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
		teacher.setAddress(resultSet.getString("address"));
		teacher.setEmail(resultSet.getString("email"));
		teacher.setPhone(resultSet.getString("phone"));
		teacher.setGender(Gender.valueOf(resultSet.getString("gender")));
		return teacher;
	}

}
