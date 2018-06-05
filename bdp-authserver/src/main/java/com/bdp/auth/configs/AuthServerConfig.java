package com.bdp.auth.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	/**
	 * 指定一个加密器，这处很重要，因为会在AuthServerSecurityConfig设置passwordEncoder，
	 * 这样普通用户登录时密码的验证与资源服务器或者OAuthClient用户clientId和clientSecret登录时
	 * 都会用设置的passwordEncoder做校验，这与spring security登录机制重用有关系。算是一坑。
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("bdp").secret(passwordEncoder().encode("bdp"))
				.authorizedGrantTypes("authorization_code", "refresh_token").scopes("all").autoApprove(true)// 很有用，自动授权，跳过认证服务器的授权页面，提升用户体验
				.and().withClient("third").secret("third").authorizedGrantTypes("authorization_code", "refresh_token")
				.scopes("all").autoApprove(true);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
	}

	/**
	 * 认证服务器的安全配置
	 * 
	 * @param security
	 * @throws Exception
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// 该配置表示在使用JWT时客户端或资源服务器在获取token_key时需要认证，即访问oauth/token_key路径。
		// 此时客户端会把clientId和clientSecret发到认证服务器上做认证。
		security.tokenKeyAccess("isAuthenticated()");
	}

	@Bean
	public TokenStore jwtTokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		// 客户端或资源服务器通过oauth/token_key路径获取该值
		converter.setSigningKey("bdp-auth-token-key");
		return converter;
	}
}
