package com.nesterov.university.dao;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import com.nesterov.university.model.Audience;

@Component
public class AudienceDao {

	private static final String SELECT_AUDIENCE = "SELECT *  FROM audiences WHERE id = ?";
	private static final String DELETE = "DELETE FROM audiences WHERE id = ?";
	
	@Autowired
	private JdbcTemplate template;

	@Autowired
	private SimpleJdbcInsert insertAudience;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.insertAudience = insertAudience.withTableName("audiences").usingGeneratedKeyColumns("id");
	}

	public void create(Audience audience) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(audience);
		Number newId = insertAudience.executeAndReturnKey(parameters);
		audience.setId(newId.longValue());
	}

	public Audience read(long id) {
		return template.queryForObject(SELECT_AUDIENCE, new Object[] { id },
				new BeanPropertyRowMapper<Audience>(Audience.class));
	}
	
	public boolean delete(long id){
	    Object[] args = new Object[] {id};
	    return template.update(DELETE, args) == 1;
	}
}
