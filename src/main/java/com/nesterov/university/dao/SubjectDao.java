package com.nesterov.university.dao;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import com.nesterov.university.model.Subject;

@Component
public class SubjectDao {

	private static final String SELECT_SUBJECT = "SELECT *  FROM subjects WHERE id = ?";
	private static final String DELETE = "DELETE FROM subjects WHERE id = ?";
	
	@Autowired
	private JdbcTemplate template;

	@Autowired
	private SimpleJdbcInsert insertSubject;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.insertSubject = insertSubject.withTableName("subjects").usingGeneratedKeyColumns("id");
	}

	public void create(Subject subject) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(subject);
		Number newId = insertSubject.executeAndReturnKey(parameters);
		subject.setId(newId.longValue());
	}

	public Subject read(long id) {
		return template.queryForObject(SELECT_SUBJECT, new Object[] { id },
				new BeanPropertyRowMapper<Subject>(Subject.class));
	}
	
	public boolean delete(long id){
	    Object[] args = new Object[] {id};
	    return template.update(DELETE, args) == 1;
	}
}
