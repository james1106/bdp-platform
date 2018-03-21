package com.bdp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.auth.BasicAuthRequestInterceptor;

@Configuration
public class FeignSecurityConfiguration {

	/**
	 * 这种定法为Feign客户端做的设置是对所有客户端有效的
	 * 
	 * @return
	 */
	@Bean
	public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
		return new BasicAuthRequestInterceptor("admin", "password");
	}
}
