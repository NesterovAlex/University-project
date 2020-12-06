package com.nesterov.university.dao;

import java.sql.PreparedStatement;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.mapper.AudienceRowMapper;
import com.nesterov.university.model.Audience;

@Component
public class AudienceDao {

	private static final String SELECT_BY_ROOM_NUMBER = "SELECT * FROM audiences WHERE room_number = ?";
	private static final String SELECT_BY_ID = "SELECT * FROM audiences WHERE id = ?";
	private static final String INSERT = "INSERT INTO audiences (room_number, capacity) values (?, ?)";
	private static final String UPDATE = "UPDATE audiences SET room_number = ?, capacity = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM audiences WHERE id = ?";
	private static final String SELECT = "SELECT * FROM audiences";

	private AudienceRowMapper audienceRowMapper;	
	private JdbcTemplate jdbcTemplate;

	public AudienceDao(JdbcTemplate template, AudienceRowMapper audienceRowMapper) {
		this.jdbcTemplate = template;
		this.audienceRowMapper = audienceRowMapper;
	}

	public void create(Audience audience) {
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setInt(1, audience.getRoomNumber());
			statement.setInt(2, audience.getCapacity());
			return statement;
		}, holder);
		audience.setId(holder.getKey().longValue());
	}

	public Audience get(long id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, audienceRowMapper);
	}

	
	public void delete(long id) {
		jdbcTemplate.update(DELETE, id);
	}

	public void update(Audience audience) {
		jdbcTemplate.update(UPDATE, audience.getRoomNumber(), audience.getCapacity(), audience.getId());
	}

	public List<Audience> findAll() {
		return jdbcTemplate.query(SELECT, audienceRowMapper);
	}
	
	public Audience findByRoomNumber(long roomNumber) {
		Audience audience = null;
		try {
			audience = jdbcTemplate.queryForObject(SELECT_BY_ROOM_NUMBER, new Object[] { roomNumber }, audienceRowMapper);
		} catch (EmptyResultDataAccessException e) {
			e.getMessage();
		}
		return audience;
	}
}
