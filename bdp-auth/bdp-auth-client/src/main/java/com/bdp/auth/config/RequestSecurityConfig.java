package com.bdp.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 配置ResourceServer的安全，通过对auth.client.permit-patterns注入，来控制哪些路径不需要认证
 * 参考ResourceServerConfiguration中的代码
 * 
 * @author jack
 *
 */
@Configuration
public class RequestSecurityConfig extends ResourceServerConfigurerAdapter {

	@Value("${auth.client.permit-patterns}")
	private String[] permitPatterns;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		if (permitPatterns != null && permitPatterns.length > 0) {
			http.authorizeRequests().antMatchers(permitPatterns).permitAll().anyRequest().authenticated();
		}
	}
}
