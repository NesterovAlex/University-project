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
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.dao.mapper.AudienceRowMapper;
import com.nesterov.university.model.Audience;

@Component
public class AudienceDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AudienceDao.class);

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
		LOGGER.debug("Creating '{}'", audience);
		final KeyHolder holder = new GeneratedKeyHolder();
		int affectedRows = jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setInt(1, audience.getRoomNumber());
			statement.setInt(2, audience.getCapacity());
			return statement;
		}, holder);
		audience.setId(holder.getKey().longValue());
		if (affectedRows == 0) {
			String message = format("Audience '%s' not created ", audience);
			LOGGER.error(message);
			throw new NotCreateException(message);
		} else {
			LOGGER.trace("Successfully created '{}'", audience);
		}
	}

	public Audience get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting adience by id = '{}'", id);
		Audience audience = new Audience();
		try {
			audience = jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, audienceRowMapper);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(audience.toString());
			String message = format("Audience with id '%s' not found", id);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			LOGGER.error(audience.toString());
			String message = format("Unable to get Audience with id '%s'", id);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Founded '{}'", audience);
		return audience;
	}

	public void delete(long id) throws NotExistException {
		LOGGER.debug("Deleting adience by id = '{}'", id);
		int affectedRows = jdbcTemplate.update(DELETE, id);
		if (affectedRows == 0) {
			LOGGER.error("Audience was not deleted");
			String message = format("Audience with id = '%s' not exist ", id);
			throw new NotExistException(message);
		} else {
			LOGGER.trace("Deleted audience with id = '{}'", id);
		}
	}

	public void update(Audience audience) throws NotCreateException {
		LOGGER.debug("Updating adience '{}'", audience);
		int affectedRows = jdbcTemplate.update(UPDATE, audience.getRoomNumber(), audience.getCapacity(),
				audience.getId());
		if (affectedRows == 0) {
			LOGGER.error("Audience was not updated");
			String message = format("Audience '%s' not updated", audience);
			throw new NotCreateException(message);
		} else {
			LOGGER.trace("Updated '{}'", audience);
		}
	}

	public List<Audience> findAll() throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting all audiences");
		List<Audience> audiences = null;
		try {
			audiences = jdbcTemplate.query(SELECT, audienceRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = "No Audiences";
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = "Unable to get Audiences";
			LOGGER.error(message);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all audiences");
		return audiences;
	}

	public Audience findByRoomNumber(long roomNumber) throws EntityNotFoundException, QueryNotExecuteException {
		LOGGER.debug("Getting adience by rommNumber = '{}'", roomNumber);
		Audience audience = null;
		try {
			audience = jdbcTemplate.queryForObject(SELECT_BY_ROOM_NUMBER, new Object[] { roomNumber },
					audienceRowMapper);
		} catch (EmptyResultDataAccessException e) {
			String message = format("Audience with room number '%s' not found", roomNumber);
			LOGGER.error(message);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			LOGGER.error("Audience not found");
			String message = format("Unable to get Audience with id '%s'", roomNumber);
			throw new QueryNotExecuteException(message, e);
		}
		LOGGER.trace("Finded all audiences by room number");
		return audience;
	}
}
