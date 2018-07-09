package com.bdp.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;

@Configuration
public class MyConfiguration {

	@Bean
	public ResourceServerConfiguration resourceServerConfiguration() {
		return new ResourceServerConfiguration();
	}
}
