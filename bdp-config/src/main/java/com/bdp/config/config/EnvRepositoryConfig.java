package com.bdp.config.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.bdp.config.biz.PropertyBiz;
import com.bdp.config.impl.DBEnvironmentRepository;

@Configuration
@Profile("db")
public class EnvRepositoryConfig {
	@Bean
	public DBEnvironmentRepository mybatisEnvironmentRepository(PropertyBiz propertyBiz) {
		return new DBEnvironmentRepository(propertyBiz);
	}
}
