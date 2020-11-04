package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.model.Group;

@Component
public class GroupDao {

	private static final String INSERT = "INSERT INTO groups (name) values (?)";
	private static final String SELECT = "SELECT *  FROM groups WHERE id = ?";
	private static final String UPDATE = "UPDATE groups SET name = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM groups WHERE id = ?";

	private JdbcTemplate template;

	public GroupDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(Group group) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, group.getName());
			return statement;
		}, holder);
		group.setId(holder.getKey().longValue());
	}

	public Group get(long id) {
		return template.queryForObject(SELECT, new Object[] { id },
				(resultSet, rowNum) -> new Group(resultSet.getLong("id"), resultSet.getString("name")));
	}

	public void delete(long id) {
		template.update(DELETE, id);
	}

	public void update(Group group) {
		template.update(UPDATE, group.getName(), group.getId());
	}
}
