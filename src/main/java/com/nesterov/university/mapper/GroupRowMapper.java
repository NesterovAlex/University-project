package com.nesterov.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.model.Group;

@Component
public class GroupRowMapper implements RowMapper<Group>{

	@Autowired
	private StudentDao dao;
	
	@Override
	public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
		Group group = new Group();
		group.setId(rs.getLong("id"));
		group.setName(rs.getString("name"));
		group.setStudents(dao.getAllByGroup(rs.getLong("id")));
		return group;
	}

}
