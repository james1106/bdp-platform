package com.bdp.config.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.bdp.config.biz.PropertyBiz;
import com.bdp.config.impl.MyBatisEnvironmentRepository;

@Configuration
@Profile("mybatis")
public class EnvRepositoryConfig {
	@Bean
	public MyBatisEnvironmentRepository mybatisEnvironmentRepository(PropertyBiz propertyBiz) {
		return new MyBatisEnvironmentRepository(propertyBiz);
	}
}
