package com.bdp.metadata.remoteapi;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author jack
 */
@FeignClient(name = "bdp-datasource")
public interface DataSourceClient {
	@GetMapping("/dataSource/{id}")
	public String getDataSourceById(@PathVariable("id") String id);
}
