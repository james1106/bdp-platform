package com.bdp.tm.listener;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bdp.tm.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ApplicationStartListener implements ApplicationListener<ServletWebServerInitializedEvent> {

	@Override
	public void onApplicationEvent(ServletWebServerInitializedEvent event) {
		int serverPort = event.getWebServer().getPort();
		String ip = getIp();
		Constants.address = ip + ":" + serverPort;
	}

	private String getIp() {
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return host;
	}
}
