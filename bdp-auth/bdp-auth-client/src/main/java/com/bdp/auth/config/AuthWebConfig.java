package com.bdp.auth.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bdp.auth.context.AuthContextHandler;

@Configuration
public class AuthWebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserBindInterceptor());
	}

	/**
	 * 登录用户绑定到当前线程中，方便使用
	 */
	class UserBindInterceptor implements HandlerInterceptor {
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof OAuth2Authentication) {
				OAuth2AuthenticationDetails detail = (OAuth2AuthenticationDetails) authentication.getDetails();
				String token = detail.getTokenValue();
			}
			return true;
		}

		/**
		 * 清空线程变量
		 */
		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
				Exception ex) throws Exception {
			AuthContextHandler.remove();
		}
	}
}
