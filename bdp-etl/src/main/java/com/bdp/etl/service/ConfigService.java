package com.bdp.etl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import feign.hystrix.FallbackFactory;

/**
 * 注意，这里如果bdp-config这个服务的server.servlet.context-path设置了值 config， 则name需要加上这个值,
 * 否则无法找到服务。如：bdp-config/config. 要启用fallback功能需要配置feign.hystrix.enabled=true属性。
 * 当fallback与fallbackFactory同时使用时，只有fallback有用。这里为了测试两个都配置。
 * 建议使用fallbackFactory，可以获取失败的异常信息,注意体会/outApi的配置，当前然这个也可以放在FeignClient上
 * 主要是路径要对，最佳实践，最好放在方法上面。。
 * 
 * @author jack
 */
@FeignClient(name = "bdp-config", fallback = ConfigService.FallBack.class, fallbackFactory = ConfigService.FallBackFactory.class)
public interface ConfigService {

	@GetMapping("/outApi/hello/{name}")
	public String sayHello(@PathVariable("name") String name);

	@Component
	static class FallBack implements ConfigService {
		@Override
		public String sayHello(String name) {
			return "Call bdp-config/outApi/hello/{name} Fallback!";
		}

	}

	@Component
	static class FallBackFactory implements FallbackFactory<ConfigService> {
		
		@Autowired
		ConfigService.FallBack fallBack;
		
		@Override
		public ConfigService create(Throwable cause) {
			cause.printStackTrace();
			return fallBack;
		}
	}
}
