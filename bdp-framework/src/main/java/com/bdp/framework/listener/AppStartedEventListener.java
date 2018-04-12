package com.bdp.framework.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * Spring boot 启动监听类<br/>
 * 在这个类中添加Thymeleaf页面可用的一些常量
 * 
 * @author JACK
 */
public class AppStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {

	@Autowired
	private ThymeleafViewResolver viewResolver;

	@Value("${bdp-platform.proxy-mapping:}")
	private String proxyMapping;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		if (viewResolver != null) {
			viewResolver.addStaticVariable("proxy", "/" + proxyMapping);
		}
	}
}