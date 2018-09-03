package com.bdp.tx;

import feign.RequestInterceptor;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.bdp.tx.springcloud.feign.TransactionRestTemplateInterceptor;
import com.bdp.tx.springcloud.http.TransactionHttpRequestInterceptor;

/**
 * 为feign及RestTemplate添加请求拦截器的配置，在发出请求时自动添加事务组ID的信息
 */
@Configuration
public class RequestInterceptorConfiguration {

	/**
	 * 为容器注入RequestInterceptor，feign客户端会自动应用当前的拦截器
	 * 
	 * @return
	 */
	@Bean
	public RequestInterceptor requestInterceptor() {
		return new TransactionRestTemplateInterceptor();
	}

	/**
	 * 为容器注入ClientHttpRequestInterceptor，这个拦截器是给RestTemplate使用的<br/>
	 * 但是其无法自动应用，还需要为每个RestTemplate客户端分别设置，所以在@PostConstruct注解的方法中进行了设置
	 * 
	 * @return
	 */
	@Bean
	public ClientHttpRequestInterceptor httpInterceptor() {
		return new TransactionHttpRequestInterceptor();
	}

	/**
	 * 通过spring boot查看所有的RestTemplate实例，放在一个集合中
	 */
	@Autowired(required = false)
	private List<RestTemplate> restTemplates;

	@PostConstruct
	public void init() {
		// 对所有的RestTemplate实例添加请求拦截器，在发出请求时添加上事务组ID信息
		if (restTemplates != null) {
			for (RestTemplate restTemplate : restTemplates) {
				restTemplate.setInterceptors(Collections.singletonList(httpInterceptor()));
			}
		}
	}
}
