package com.bdp.metadata.outerservices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("bdp-datasource")
public interface DataSourceService {

	@GetMapping("/dataSource/{id}")
	public String getDataSourceById(@PathVariable("id") String id);

}
