package com.bdp.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration("frameworkWebConfig")
public class WebConfig extends WebMvcConfigurerAdapter {
	Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		ResourceHandlerRegistration registration = registry.addResourceHandler("/framework/**");
		registration.addResourceLocations("classpath:/static/framework/", "classpath:/static/framework/");
		super.addResourceHandlers(registry);
	}
}
