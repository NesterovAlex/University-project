package com.nesterov.university.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


import com.nesterov.university.configuration.ApplicationConfig;

@Configuration
@Import({ApplicationConfig.class})
public class TestConfig {

	@Bean
	DataSource dataSource(EmbeddedDatabaseBuilder embeddedDatabaseBuilder) {
		return embeddedDatabaseBuilder.build();
	}
	
	@Bean
	EmbeddedDatabaseBuilder embeddedDatabaseBuilder() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2);
	}
}
