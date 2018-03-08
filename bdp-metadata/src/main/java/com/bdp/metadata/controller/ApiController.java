package com.bdp.metadata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdp.metadata.remoteapi.DataSourceClient;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@RestController
@RequestMapping("/")
public class ApiController {

	@Autowired
	private DataSourceClient dataSourceClient;
	
	//注意这块使用的是EurekaClient，开始很容易错用成DiscoveryClient.以前版本支持。一个大水坑
	@Autowired
	private EurekaClient  discoveryClient;

	@GetMapping("/dsMetadata/{dsId}")
	public String dsMetadata(@PathVariable("dsId") String dsId) {
		String dsName = dataSourceClient.getDataSourceById(dsId);
		return "Return DataSource [" + dsName + "] MetaData.";
	}
	
	@GetMapping("/instances/{id}")
	public List<InstanceInfo> instances(@PathVariable("id") String id) {
		List<InstanceInfo> infos = discoveryClient.getInstancesById(id);
        return infos;
    }
}
