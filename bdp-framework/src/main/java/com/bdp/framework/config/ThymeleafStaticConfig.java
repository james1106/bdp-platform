package com.bdp.framework.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
public class ThymeleafStaticConfig {

	@Autowired
	private ThymeleafViewResolver viewResolver;

	@Value("${bdp-platform.proxy-mapping:}")
	private String proxyMapping;

	@PostConstruct
	public void init() {
		viewResolver.addStaticVariable("proxy", proxyMapping);
	}
}
