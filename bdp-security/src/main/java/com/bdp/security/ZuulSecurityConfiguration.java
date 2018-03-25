package com.bdp.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@ConditionalOnBean({ BdpSecurityConfiguration.class })
@ConditionalOnProperty(value = "zuul.security.disabled", matchIfMissing = false)
public class ZuulSecurityConfiguration {

	@Autowired
	private BdpSecurityConfiguration bdpSecurity;

	// 这种在指定的过滤器前面添加Filter的方式可以做验证器的校验，即如果输入用户名密码及图片验证码时
	@Bean
	public Filter zuulAutoSecurityFilter() throws Exception {
		Filter filter = new ZuulAutoSecurityFilter();
		bdpSecurity.httpSecurity().addFilterBefore(filter, BasicAuthenticationFilter.class);
		return filter;
	}

	class ZuulAutoSecurityFilter implements Filter {

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {

		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException {
			ZuulHttpServletRequestWrapper zuulRequest = new ZuulHttpServletRequestWrapper((HttpServletRequest) request);
			// 为所有的请求都加上认证信息，以绕过登录
			zuulRequest.setHeader("Authorization", getBase64Credentials("admin", "password"));
			chain.doFilter(zuulRequest, response);
		}

		@Override
		public void destroy() {

		}

		private String getBase64Credentials(String username, String password) {
			String plainCreds = username + ":" + password;
			byte[] plainCredsBytes = plainCreds.getBytes();
			byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
			return "Basic "+new String(base64CredsBytes);
		}
	}

	public class ZuulHttpServletRequestWrapper extends HttpServletRequestWrapper {

		private Map<String, String> headers;

		public ZuulHttpServletRequestWrapper(HttpServletRequest request) {
			super(request);
			this.headers = new HashMap<>();
		}

		public void setHeader(String name, String value) {
			this.headers.put(name, value);
		}

		public String getHeader(String name) {
			String value = this.headers.get(name);
			if (value != null) {
				return value;
			}
			return super.getHeader(name);
		}
		
		/**
		 * 这一个方法很重要，因为zuul向下游传递请求头时候调用的是当前方法
		 */
		public Enumeration<String> getHeaders(String name) {
			String value = this.headers.get(name);
			Set<String> set = new HashSet<String>();
			if (value != null) {
				set.add(value);
				return Collections.enumeration(set);
			}
			return super.getHeaders(name);
		}

		public Enumeration<String> getHeaderNames() {
			Set<String> set = new HashSet<String>(headers.keySet());
			Enumeration<String> enumeration = super.getHeaderNames();
			while (enumeration.hasMoreElements()) {
				String name = enumeration.nextElement();
				set.add(name);
			}
			return Collections.enumeration(set);
		}
	}

}
