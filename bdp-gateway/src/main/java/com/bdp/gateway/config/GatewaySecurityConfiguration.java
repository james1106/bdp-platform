package com.bdp.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
// 这个注解是不是放在WebSecurityConfigurerAdapter的接口有也是有区别的，参考注解的实现
// 另外这个注解引入的JAR中可以将token放入到zuul请求头中发送到下游服务，不需要额外处理。
@EnableOAuth2Sso
public class GatewaySecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${security.oauth2.client.logout-uri}")
	private String logoutUri;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 配置登出路径为auth认证服务器的请求地址，用于清除认证服务器上的登录信息，认证服务器会自动转回当前系统
		http.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl(logoutUri)
				.deleteCookies()
				.invalidateHttpSession(true)
				.permitAll()
			.and()
				.antMatcher("/**")
				.authorizeRequests()
				.anyRequest()
				.authenticated()
			.and()
				.csrf()
				.disable();
	}

}
