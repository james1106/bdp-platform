package com.bdp.etl.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;

/*
 * 这个类为了演示从配置服务器刷新配置的功能。
 * 没有使用网上介绍的整合Spring cloud bus，太麻烦。单独安装MQ服务器，部署也不方便。
 * 参考了org.springframework.cloud.config.client.ConfigClientWatch的实现。
 * 目前ConfigClientWatch使用惹来比较麻烦，且在生产环境使用并不合适。
 * 
 * 在自定义的controller类中注入ContextRefresher
 * @Autowired
 * private ContextRefresher refresher;
 * 
 * 通过调用 refresher.refresh();可以手动刷新配置
 * 
 * ContextRefresher的Bean生产参考
 * org.springframework.cloud.autoconfigure.RefreshAutoConfiguration
 * 即ContextRefresher可直接注入使用
 * 
 */
@Configuration
public class RefreshBeanConfig {
	
	@Autowired
	private ResourceServerConfiguration resourceServerConfiguration;

	@PostConstruct
	public void init() {
		System.out.println(resourceServerConfiguration);
	}
	
	// 需要刷新的Bean必须加上注解@RefreshScope
	@Bean
	@RefreshScope
	public RefreshObj getRefreshObj(Environment env) {
		RefreshObj obj = new RefreshObj();
		obj.setYear(Integer.parseInt(env.getProperty("login.current.year", "2000")));
		obj.setUserRole(env.getProperty("login.user.role", "guest"));
		return obj;
	}

}
