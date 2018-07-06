package com.bdp.datasource.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdp.datasource.configs.RefreshObj;
import com.bdp.datasource.service.ConfigService;

@RestController
@RequestMapping("/ds")
public class ApiController {
	
	@Autowired
	private ContextRefresher refresher;
	
	@Autowired
	private ConfigService configService;

	// 演示从配置服务读取配置信息,$符号取属性值，#符号计算表达示
	@Value("${login.current.year}")
	private int year;

	// 用来测试配置中心共用配置文件的情况
	@Value("${login.user.role}")
	private String userRole;

	@Autowired
	private RefreshObj refreshObj;

	@GetMapping("/list")
	public List<String> list() {
		List<String> list = new ArrayList<String>();
		list.add("Jack");
		list.add("Rose");
		return list;
	}
	
	
	@GetMapping("/refresh")
	public String refresh() {
		refresher.refresh();
		return "success";
	}

	@GetMapping("/config")
	public String getDataSourceById() throws Exception {
		return "Return " + refreshObj.getUserRole() + " " + refreshObj.getYear();
	}
	
	@GetMapping("/hello/{name}")
	public String sayHello(@PathVariable("name")String name) throws Exception {
		return configService.sayHello(name);
	}
}
