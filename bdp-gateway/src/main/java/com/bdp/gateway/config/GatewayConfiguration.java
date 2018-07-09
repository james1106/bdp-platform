package com.bdp.gateway.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.bdp.gateway.impl.CustomRouteLocator;

//@Configuration
public class GatewayConfiguration {

	@Autowired
	ZuulProperties zuulProperties;

	@Autowired
	ServerProperties server;

	@Autowired
	List<WebSecurityConfigurerAdapter> securityAdapters;

	@PostConstruct
	public void init() {
		if(securityAdapters!=null) {//OAuth2SsoDefaultConfiguration
			for (WebSecurityConfigurerAdapter adapter : securityAdapters) {
				System.out.println(adapter);
			}
		}
	}

	@Bean
	public CustomRouteLocator routeLocator() {
		CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServlet().getServletPrefix(),
				this.zuulProperties);
		return routeLocator;
	}
}
