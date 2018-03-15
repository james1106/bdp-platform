package com.bdp.config.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@RestController
@RequestMapping("/")
public class ApiController {

	// 注意这块使用的是EurekaClient，开始很容易错用成DiscoveryClient.以前版本支持。一个大水坑
	@Autowired
	private EurekaClient discoveryClient;

	@GetMapping("/refresh/{app}")
	public String refresh(@PathVariable("app") String app) {
		if (StringUtils.isEmpty(app)) {
			return "end";
		}
		Application application = discoveryClient.getApplication(app);
		List<InstanceInfo> infos = application.getInstances();
		for (InstanceInfo instanceInfo : infos) {
			Map<String, String> metas = instanceInfo.getMetadata();
			//演示元数据的使用，这些元数据在服务提供方填写的。可以用来做很多有用的功能。
			for (String key : metas.keySet()) {
				System.out.println("Key:" + key + "  Value:" + metas.get(key));
			}
			String refreshPath = instanceInfo.getHomePageUrl() + "/refresh";
			try {
				RestTemplate restTemplate = new RestTemplate();
				String result = restTemplate.getForObject(new URI(refreshPath), String.class);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
		}
		return "success";
	}
}