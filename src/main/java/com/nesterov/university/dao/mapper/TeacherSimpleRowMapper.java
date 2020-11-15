package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherSimpleRowMapper implements RowMapper<Teacher>{

	@Override
	public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
		Teacher teacher = new Teacher();
		teacher.setId(rs.getLong("id"));
		teacher.setFirstName(rs.getString("first_name"));
		teacher.setLastName(rs.getString("last_name"));
		teacher.setBirthDate(rs.getObject("birth_date", LocalDate.class));
		teacher.setAddress(rs.getString("address"));
		teacher.setEmail(rs.getString("email"));
		teacher.setPhone(rs.getString("phone"));
		teacher.setGender(Gender.valueOf(rs.getString("gender")));
		return teacher;
	}
}
