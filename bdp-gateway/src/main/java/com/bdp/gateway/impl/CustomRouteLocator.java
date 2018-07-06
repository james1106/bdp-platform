package com.bdp.gateway.impl;

import java.util.Map;

import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;

public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

	public CustomRouteLocator(String servletPath, ZuulProperties properties) {
		super(servletPath, properties);
	}

	@Override
	protected Map<String, ZuulRoute> locateRoutes() {
		return super.locateRoutes();
	}

	@Override
	public void refresh() {
		doRefresh();
	}
}
