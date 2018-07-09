package com.bdp.etl.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;

@Configuration
@ConditionalOnBean({ ResourceServerConfiguration.class })
public class MyOnBeanConfiguration {

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	public void init() {
		System.out.println(dataSource);
	}
}
