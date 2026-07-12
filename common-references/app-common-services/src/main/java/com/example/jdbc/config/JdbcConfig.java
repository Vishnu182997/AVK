package com.example.jdbc.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties", "classpath:db.properties" })
@ComponentScan({ "com.fastcollab.config", "com.fastcollab.dao", "com.fastcollab.service",
		"com.fastcollab.handler.service" })
public class JdbcConfig {

	@Autowired
	private Environment environment;

	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		basicDataSource.setUrl(environment.getRequiredProperty("jdbc.connection.url"));
		basicDataSource.setUsername(environment.getRequiredProperty("jdbc.connection.username"));
		basicDataSource.setPassword(environment.getRequiredProperty("jdbc.connection.password"));
		return basicDataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public PlatformTransactionManager txManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
