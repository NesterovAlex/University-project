package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.StudentDao;
import com.nesterov.university.model.Group;

@Component
public class GroupRowMapper implements RowMapper<Group> {

	private StudentDao studentDao;

	public GroupRowMapper(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	@Override
	public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
		Group group = new Group();
		group.setId(rs.getLong("id"));
		group.setName(rs.getString("name"));
		group.setStudents(studentDao.findByGroupId(rs.getLong("id")));
		return group;
	}
}
