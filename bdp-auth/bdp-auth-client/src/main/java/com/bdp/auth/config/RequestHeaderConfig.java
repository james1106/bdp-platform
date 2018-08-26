package com.bdp.auth.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import com.bdp.auth.constant.AuthClientConstant;
import com.bdp.auth.context.AuthContextHandler;

import feign.RequestInterceptor;

/**
 * 为所有的feign客户端请求及RestTemplate的请求的header中加上jwt token
 * 
 * @author jack
 *
 */
@Configuration
public class RequestHeaderConfig {

	@Autowired(required = false)
	private List<RestTemplate> restTemplates;

	@Bean
	public RequestInterceptor headerInterceptor() {
		return template -> {
			// 注意这里，如果Feign使用了hystrix，且选择了线程池隔离，则AuthContextHandler通过。
			// ThreadLocal绑定的变量是获取不到的，改用InheritableThreadLocal来绑定线程变量也可能获取不到，
			// InheritableThreadLocal只能获取其父线程设置的变量，hystrix中从线程池中拿到的线程可能是由其它线程创建的
			String jwtToken = (String) AuthContextHandler.get(AuthClientConstant.JWTTOKEN_IN_THREAD_KEY);
			if (StringUtils.isNotEmpty(jwtToken)) {
				template.header("Authorization", jwtToken);
			}
		};
	}

	@Bean
	public ClientHttpRequestInterceptor httpHeaderInterceptor() {
		return new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				String jwtToken = (String) AuthContextHandler.get(AuthClientConstant.JWTTOKEN_IN_THREAD_KEY);
				if (StringUtils.isNotEmpty(jwtToken)) {
					HttpHeaders headers = request.getHeaders();
					headers.add("Authorization", jwtToken);
				}
				return execution.execute(request, body);
			}
		};
	}

	@PostConstruct
	public void init() {
		if (restTemplates != null) {
			for (RestTemplate restTemplate : restTemplates) {
				// 添加拦截器，在拦截器中加上jwt token信息
				restTemplate.setInterceptors(Collections.singletonList(httpHeaderInterceptor()));
			}
		}
	}
}
