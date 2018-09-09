package com.bdp.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * 配置token的过期自动刷新，参考OAuth2TokenRelayFilter代码
 * 
 * @author jack
 *
 */
@Configuration
public class OAuth2ClientConfiguration {

	@Autowired
	OAuth2ProtectedResourceDetails auth2ProtectedResourceDetails;

	@Bean
	public OAuth2RestOperations restTemplate(OAuth2ClientContext oauth2ClientContext) {
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(auth2ProtectedResourceDetails, oauth2ClientContext);
		return restTemplate;
	}
}
