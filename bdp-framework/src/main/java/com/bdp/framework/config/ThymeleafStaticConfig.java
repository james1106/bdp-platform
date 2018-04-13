package com.bdp.framework.config;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
public class ThymeleafStaticConfig {

	@Autowired
	private ThymeleafViewResolver viewResolver;

	@Value("${bdp-platform.zuul-proxy:}")
	private String zuulProxy;

	@PostConstruct
	public void init() {
		if (StringUtils.isNotEmpty(zuulProxy)) {
			viewResolver.addStaticVariable("zuulProxy", "/" + zuulProxy);
		} else {
			viewResolver.addStaticVariable("zuulProxy", "");
		}
	}
}
