package com.bdp.auth.config;

import java.lang.reflect.Field;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.bdp.auth.constant.AuthClientConstant;
import com.bdp.auth.context.AuthContextHandler;
import com.bdp.auth.context.AuthUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Configuration
public class AuthClientWebConfig implements WebMvcConfigurer {

	@Autowired
	private JwtAccessTokenConverter JwtAccessTokenConverter;

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
			if (!(handler instanceof ResourceHttpRequestHandler)) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication instanceof OAuth2Authentication) {
					OAuth2AuthenticationDetails detail = (OAuth2AuthenticationDetails) authentication.getDetails();
					String token = detail.getTokenValue();
					String signingKey = getSigningKey();
					JwtParser parser = Jwts.parser().setSigningKey(signingKey.getBytes("utf-8"));
					Claims claims = parser.parseClaimsJws(token).getBody();
					String userId = claims.get("userId", String.class);
					String username = claims.get("username", String.class);
					String realname = claims.get("realname", String.class);
					String sex = claims.get("sex", String.class);
					String image = claims.get("image", String.class);
					Date birthday = claims.get("birthday", Date.class);
					String address = claims.get("address", String.class);
					AuthUser user = new AuthUser(userId, username, realname, sex, birthday, image, address);
					AuthContextHandler.setAuthUser(user);
					AuthContextHandler.set(AuthClientConstant.JWTTOKEN_IN_THREAD_KEY,
							detail.getTokenType() + " " + token);
				}
			}
			return true;
		}

		/**
		 * 通过反射获取私有属性
		 * 
		 * @return
		 */
		private String getSigningKey() {
			Field field = ReflectionUtils.findField(JwtAccessTokenConverter.class, "signingKey");
			ReflectionUtils.makeAccessible(field);
			String signingKey = (String) ReflectionUtils.getField(field, JwtAccessTokenConverter);
			return signingKey;
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
