package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.mapper.AudienceRowMapper;
import com.nesterov.university.mapper.LessonRowMapper;
import com.nesterov.university.model.Audience;

@Component
public class AudienceDao {

	private static final String SELECT_BY_ID = "SELECT *  FROM audiences WHERE id = ?";
	private static final String INSERT = "INSERT INTO audiences (room_number, capacity) values (?, ?)";
	private static final String UPDATE = "UPDATE audiences SET room_number = ?, capacity = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM audiences WHERE id = ?";
	private static final String SELECT = "SELECT * FROM audiences";

	@Autowired
	private AudienceRowMapper audienceRowMapper;
	
	private JdbcTemplate template;

	public AudienceDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(Audience audience) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setInt(1, audience.getRoomNumber());
			statement.setInt(2, audience.getCapacity());
			return statement;
		}, holder);
		audience.setId(holder.getKey().longValue());
	}

	public Audience get(long id) {
		return template.queryForObject(SELECT_BY_ID, new Object[] { id }, new AudienceRowMapper());
	}

	public void delete(long id) {
		template.update(DELETE, id);
	}

	public void update(Audience audience) {
		template.update(UPDATE, audience.getRoomNumber(), audience.getCapacity(), audience.getId());
	}

	public List<Audience> getAll() {
		return template.query(SELECT, new AudienceRowMapper());
	}

	public AudienceRowMapper getAudienceRowMapper() {
		return audienceRowMapper;
	}

	@Autowired
	public void setAudienceRowMapper(AudienceRowMapper audienceRowMapper) {
		this.audienceRowMapper = audienceRowMapper;
	}
}
