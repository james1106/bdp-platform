package com.bdp.metadata.remoteapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bdp.metadata.fallback.DataSourceClientFallback;
import com.bdp.metadata.fallback.DataSourceClientFallbackFactory;

/**
 * 注意，这里如果bdp-datasource这个服务的server.servlet.context-path设置了值 ， 则name需要加上这个值,
 * 否则无法找到服务。如：bdp-datasource/datasource.
 * 要启用fallback功能需要配置feign.hystrix.enabled=true属性。
 * 当fallback与fallbackFactory同时使用时，只有fallback有用。这里为了测试两个都配置。
 * 建议使用fallbackFactory，可以获取失败的异常信息
 * 
 * 用用的网址：http://blog.csdn.net/forezp/article/details/70148833
 * 
 * @author jack
 */
@FeignClient(name = "bdp-datasource", fallback = DataSourceClientFallback.class, fallbackFactory = DataSourceClientFallbackFactory.class)
public interface DataSourceClient {
	@GetMapping("/dataSource/{id}")
	public String getDataSourceById(@PathVariable("id") String id);
}
