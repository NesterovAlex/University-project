package com.nesterov.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.nesterov.university.model.Audience;

@Component
public class AudienceRowMapper implements RowMapper<Audience> {

	public AudienceRowMapper() {}
	
	@Override
	public Audience mapRow(ResultSet rs, int rowNum) throws SQLException {
		Audience audience = new Audience();
		audience.setId(rs.getLong("id"));
		audience.setRoomNumber(rs.getInt("room_number"));
		audience.setCapacity(rs.getInt("capacity"));
		return audience;
	}

}
