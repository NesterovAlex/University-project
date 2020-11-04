package com.nesterov.university.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan(basePackages = "com.nesterov.university")
@PropertySource(value = "classpath:database.properties", encoding="UTF-8")
public class ApplicationConfig {

	@Value("${driver}")
	public String driver;
	
	@Value("${url}")
	public String url;

	@Value("${username}")
	public String username;
	
	@Value("${password}")
	public String password;
	
	@Bean
	public DataSource testDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername("postgres");
		dataSource.setPassword(password);
		return dataSource;
	}

	@Bean
	public JdbcTemplate testJdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		return jdbcTemplate;
	}

}