package com.nesterov.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.nesterov.university.model.Audience;

public class AudienceRowMapper implements RowMapper<Audience> {

	@Override
	public Audience mapRow(ResultSet rs, int rowNum) throws SQLException {
		Audience audience = new Audience();
		audience.setId(rs.getLong("id"));
		audience.setCapacity(rs.getInt("room_number"));
		audience.setCapacity(rs.getInt("capacity"));
		return audience;
	}

}
