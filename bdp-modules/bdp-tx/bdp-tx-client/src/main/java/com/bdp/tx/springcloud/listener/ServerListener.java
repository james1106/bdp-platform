package com.bdp.tx.springcloud.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bdp.tx.listener.service.InitService;

/**
 * 在容器启动完成后初始化Netty服务，与事务管理器连接，做消息交互
 * 
 * @author jack
 */
@Component
public class ServerListener implements ApplicationListener<ServletWebServerInitializedEvent> {

	private Logger logger = LoggerFactory.getLogger(ServerListener.class);

	private int serverPort;

	@Autowired
	private InitService initService;

	@Override
	public void onApplicationEvent(ServletWebServerInitializedEvent event) {
		logger.info("onApplicationEvent -> onApplicationEvent. " + event.getWebServer());
		this.serverPort = event.getWebServer().getPort();

		initService.start();
	}

	public int getPort() {
		return this.serverPort;
	}
}
