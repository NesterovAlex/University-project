package com.nesterov.university.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.nesterov.university.mapper.AudienceRowMapper;
import com.nesterov.university.mapper.LessonRowMapper;
import com.nesterov.university.mapper.StudentRowMapper;

@Configuration
@ComponentScan(basePackages = "com.nesterov.university")
@PropertySource(value = "classpath:config.properties", encoding="UTF-8")
public class ApplicationConfig {

	@Value("${driver}")
	private String driver;
	
	@Value("${url}")
	private String url;

	@Value("${username}")
	private String username;
	
	@Value("${password}")
	private String password;
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		return jdbcTemplate;
	}

	@Bean(name="lessonRowMapper")
	public LessonRowMapper lessonRowMapper(JdbcTemplate template) {
		return new LessonRowMapper(template);
	}
	
	@Bean
	public AudienceRowMapper audienceRowMapper() {
		return new AudienceRowMapper();
	}
	
	@Bean
	public StudentRowMapper studentRowMapper() {
		return new StudentRowMapper();
	}
}
