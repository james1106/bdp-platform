package com.bdp.framework.config;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * 一种静态指定代理映射的方式，默认关掉，动态从zuul请求中获取。<br/>
 * @author jack
 *
 */
@Configuration
@ConditionalOnProperty(name = "bdp-platform.proxy-static", matchIfMissing = false)
public class ProxyStaticConfig {

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
