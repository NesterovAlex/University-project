package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Group;

@Component
public class GroupRowMapper implements RowMapper<Group> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupRowMapper.class);

	private StudentDao studentDao;

	public GroupRowMapper(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	@Override
	public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
		Group group = new Group();
		group.setId(rs.getLong("id"));
		group.setName(rs.getString("name"));
		try {
			group.setStudents(studentDao.findByGroupId(rs.getLong("id")));
		} catch (QueryNotExecuteException | EntityNotFoundException | SQLException e) {
			LOGGER.error(e.getMessage());
		}
		return group;
	}
}
