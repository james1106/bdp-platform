package com.bdp.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 一些组件在这里配置
 * @author jack
 *
 */
@Configuration
public class AuthConfiguration {

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * 自定义 配置一个DaoAuthenticationProvider，主要是因为默认的配置<br/>
	 * hideUserNotFoundExceptions的值为true,全隐藏掉用户名不存在的异常
	 * 
	 * @return
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setHideUserNotFoundExceptions(false);
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	/**
	 * 指定一个加密器，这很重要,普通用户登录时密码的验证与资源服务器或者OAuthClient用户clientId和clientSecret登录时
	 * 都会用设置的passwordEncoder做校验，这与spring security登录机制重用有关系。算是一坑。
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
