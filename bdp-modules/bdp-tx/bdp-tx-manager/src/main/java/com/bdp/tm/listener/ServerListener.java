package com.bdp.tm.listener;

import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bdp.tm.listener.service.InitService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 在Servlet容器完成后开始启动netty服务
 * 
 * @author jack
 *
 */
@Component
public class ServerListener implements ServletContextListener {

	private WebApplicationContext springContext;

	private InitService initService;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		initService = springContext.getBean(InitService.class);
		initService.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		initService.close();
	}

}
