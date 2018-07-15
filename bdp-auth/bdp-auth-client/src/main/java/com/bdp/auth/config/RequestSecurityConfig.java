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

	// https://github.com/spring-projects/spring-security-oauth/issues/517
	// 注意：这里给个默认值issues,保证configure会执行安全认证设置,因为自定义ResourceServerConfigurerAdapter时，如果在configure方法中不进行任何设置会有如上错误。<br/>
	// 原因SpringSecurity安全是通过HttpSecurity设置的，我们自定义ResourceServerConfigurerAdapter覆盖了系统默认定义却不进行设置，自然会报错。
	@Value("${auth.client.permit-patterns:/issues/**}")
	private String[] permitPatterns;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		if (permitPatterns != null && permitPatterns.length > 0) {
			http.authorizeRequests().antMatchers(permitPatterns).permitAll().anyRequest().authenticated();
		}
	}
}
