package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.nesterov.university.model.Group;

public class GroupDao {

	private static final String INSERT = "INSERT INTO groups (name) values (?)";
	private static final String SELECT = "SELECT *  FROM groups WHERE id = ?";
	private static final String UPDATE = "UPDATE groups SET name = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM groups WHERE id = ?";

	private JdbcTemplate template;

	public GroupDao(DataSource source) {
		this.template = new JdbcTemplate(source);
	}

	public long create(Group group) {
		final KeyHolder holder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setString(1, group.getName());
			return statement;
		}, holder);
		group.setId(holder.getKey().longValue());
		return group.getId();
	}

	public Group get(long id) {
		 return template.queryForObject(SELECT, new Object[]{id}, (resultSet, rowNum) ->
         new Group(
                 resultSet.getLong("id"),
                 resultSet.getString("name")
         ));
	}

	public boolean delete(long id) {
		return template.update(DELETE, id) == 1;
	}

	public long update(Group group) {
		return template.update(UPDATE, group.getName(), group.getId());
	}
}
