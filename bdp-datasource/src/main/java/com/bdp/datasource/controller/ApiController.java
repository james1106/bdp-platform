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

@RestController
@RequestMapping("/")
public class ApiController {
	
	@Autowired
	private ContextRefresher refresher;

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

	@GetMapping("/dataSource/{id}")
	public String getDataSourceById(@PathVariable("id") String id) throws Exception {
		if ("error".equals(id)) {// 如果传入的id为error,线程停3s,用来测试
			Thread.sleep(3000L);
		}
		return "Return " + refreshObj.getUserRole() + " " + refreshObj.getYear() + " 年   datasource ID:" + id + " from DataSource";
	}
}
