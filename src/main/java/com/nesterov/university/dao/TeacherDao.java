package com.nesterov.university.dao;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherDao {

	private static final String SELECT_TEACHER = "SELECT *  FROM teachers WHERE id = ?";
	private static final String DELETE = "DELETE FROM teachers WHERE id = ?";
	
	@Autowired
	private JdbcTemplate template;

	@Autowired
	private SimpleJdbcInsert insertTeacher;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.insertTeacher = insertTeacher.withTableName("teachers").usingGeneratedKeyColumns("id");
	}

	public void create(Teacher teacher) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(teacher);
		Number newId = insertTeacher.executeAndReturnKey(parameters);
		teacher.setId(newId.longValue());
	}

	public Teacher read(long id) {
		return template.queryForObject(SELECT_TEACHER, new Object[] { id },
				new BeanPropertyRowMapper<Teacher>(Teacher.class));
	}
	
	public boolean delete(long id){
	    Object[] args = new Object[] {id};
	    return template.update(DELETE, args) == 1;
	}
}