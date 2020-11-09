package com.nesterov.university.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.nesterov.university.configuration.ApplicationConfig;
import com.nesterov.university.mapper.AudienceRowMapper;
import com.nesterov.university.mapper.LessonRowMapper;

@Configuration
@ComponentScan(basePackages = "com.nesterov.university")
@Import({ApplicationConfig.class})
public class TestConfig {

	@Bean
	DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:jdbc/schema.sql").addScript("classpath:jdbc/test_data.sql").build();
	}
	
	@Bean(name="lessonRowMapper")
	public LessonRowMapper lessonRowMapper(JdbcTemplate template) {
		return new LessonRowMapper(template);
	}
}
