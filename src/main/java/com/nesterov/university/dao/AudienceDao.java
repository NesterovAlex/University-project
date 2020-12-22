package com.nesterov.university.dao;

import static java.lang.String.format;

import java.sql.PreparedStatement;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.mapper.AudienceRowMapper;
import com.nesterov.university.model.Audience;

@Component
public class AudienceDao {

	private static final Logger log = LoggerFactory.getLogger(AudienceDao.class);

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

	public void create(Audience audience) throws NotCreateException {
		log.debug("Create {}", audience);
		final KeyHolder holder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
				statement.setInt(1, audience.getRoomNumber());
				statement.setInt(2, audience.getCapacity());
				return statement;
			}, holder);
			audience.setId(holder.getKey().longValue());
		} catch (DataAccessException e) {
			String message = format("Audience '%s' not created ", audience);
			throw new NotCreateException(message, e);
		}
	}

	public Audience get(long id) throws EntityNotFoundException {
		log.debug("Get adience by id={}", id);
		Audience audience = null;
		try {
			audience = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, audienceRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Audience with id '%s' not found", id);
			throw new EntityNotFoundException(message, e);
		}
		return audience;
	}

	public void delete(long id) throws NotExistException {
		log.debug("Delete adience by id={}", id);
		try {
			jdbcTemplate.update(DELETE, id);
		} catch (DataAccessException e) {
			String message = format("Audience with id = '%s' not exist ", id);
			throw new NotExistException(message, e);
		}
	}

	public void update(Audience audience) throws NotCreateException {
		log.debug("Update adience {}", audience);
		try {
			jdbcTemplate.update(UPDATE, audience.getRoomNumber(), audience.getCapacity(), audience.getId());
		} catch (DataAccessException e) {
			String message = format("Audience '%s' not updated", audience);
			throw new NotCreateException(message, e);
		}
	}

	public List<Audience> findAll() throws EntityNotFoundException {
		log.debug("Find all audiences");
		List<Audience> audiences = null;
		try {
			audiences = jdbcTemplate.query(SELECT, audienceRowMapper);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("No Audiences", e);
		}
		return audiences;
	}

	public Audience findByRoomNumber(long roomNumber) throws EntityNotFoundException {
		log.debug("Find adience by rommNumber={}", roomNumber);
		Audience audience = null;
		try {
			audience = jdbcTemplate.queryForObject(SELECT_BY_ROOM_NUMBER, new Object[] { roomNumber },
					audienceRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Audience with room number '%s' not found", roomNumber);
			throw new EntityNotFoundException(message, e);
		}
		return audience;
	}
}
