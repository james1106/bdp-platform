package com.bdp.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.client.ClientFactory;
import com.netflix.client.IClient;
import com.netflix.loadbalancer.ILoadBalancer;

@RestController
@RequestMapping("/")
public class ApiController {

	@SuppressWarnings({ "unused", "rawtypes" })
	@GetMapping("/ribbon")
	public String ribbonInfo() {
		IClient client = ClientFactory.getNamedClient("cgtp");
		ILoadBalancer lb = ClientFactory.getNamedLoadBalancer("cgtp");
		return "SUCCESS";
	}
}
