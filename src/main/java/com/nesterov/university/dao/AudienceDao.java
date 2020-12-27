package com.nesterov.university.dao;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotUpdateException;
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
		} catch (Exception e) {
			throw new NotCreateException("Not create audience");
		}

	}

	public Optional<Audience> get(long id) {
		log.debug("Get adience by id={}", id);
		try {
			return ofNullable(jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, audienceRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}

	public void delete(long id) throws NotDeleteException {
		log.debug("Delete adience by id={}", id);
		int affectedRows = jdbcTemplate.update(DELETE, id);
		if (affectedRows == 0) {
			String message = format("Not deleted audience with id = '%s'", id);
			throw new NotDeleteException(message);
		}

	}

	public void update(Audience audience) {
		log.debug("Update adience {}", audience);
		int affectedRows = jdbcTemplate.update(UPDATE, audience.getRoomNumber(), audience.getCapacity(),
				audience.getId());
		if (affectedRows == 0) {
			throw new NotUpdateException("Audience not updated");
		}
	}

	public List<Audience> findAll() {
		log.debug("Find all audiences");
		return jdbcTemplate.query(SELECT, audienceRowMapper);
	}

	public Optional<Audience> findByRoomNumber(long roomNumber) {
		log.debug("Find adience by rommNumber={}", roomNumber);
		try {
			return ofNullable(
					jdbcTemplate.queryForObject(SELECT_BY_ROOM_NUMBER, new Object[] { roomNumber }, audienceRowMapper));
		} catch (EmptyResultDataAccessException e) {
			return empty();
		}
	}
}
