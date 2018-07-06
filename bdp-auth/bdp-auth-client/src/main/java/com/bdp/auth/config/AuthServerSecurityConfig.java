package com.bdp.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.bdp.auth.vcode.ValidateCodeSecurityConfig;

@Configuration
public class AuthServerSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 验证码校验失败处理器
	 */
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	private DaoAuthenticationProvider authenticationProvider;

	@Autowired
	ValidateCodeSecurityConfig validateCodeSecurityConfig;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 使用自定义配置的DaoAuthenticationProvider
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		String[] permits = new String[] { "/login", "/vcode/**", "/oauth/**", "/framework/**", "/static/**" };
		http.formLogin().loginPage("/login").loginProcessingUrl("/loginForm")
				.failureHandler(authenticationFailureHandler).and().apply(validateCodeSecurityConfig).and()
				.authorizeRequests().antMatchers(permits).permitAll().anyRequest().denyAll().and().csrf().disable();
	}
}