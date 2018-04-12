package com.bdp.gateway.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.client.ClientFactory;
import com.netflix.client.IClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.ILoadBalancer;

@RestController
@RequestMapping("/")
public class ApiController {
	
	@Autowired
	private EurekaClient eurekaCient;

	@SuppressWarnings({ "unused", "rawtypes" })
	@GetMapping("/ribbon")
	public String ribbonInfo() {
		IClient client = ClientFactory.getNamedClient("cgtp");
		ILoadBalancer lb = ClientFactory.getNamedLoadBalancer("cgtp");
		return "SUCCESS";
	}
	
	
	
	@SuppressWarnings({ "unused" })
	@GetMapping("/eureka")
	public String eureka() {
		Set<String> set=eurekaCient.getAllKnownRegions();
		return "SUCCESS";
	}
}
