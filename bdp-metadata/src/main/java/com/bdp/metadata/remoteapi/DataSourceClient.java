package com.bdp.metadata.remoteapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import feign.hystrix.FallbackFactory;

@FeignClient(name = "bdp-datasource/datasource",fallback=DataSourceClientFallback.class)
public interface DataSourceClient {
	@GetMapping("/dataSource/{id}")
	public String getDataSourceById(@PathVariable("id") String id);
}

class DataSourceClientFallback implements DataSourceClient{

	@Override
	public String getDataSourceById(String id) {
		return "ERROR DataSourceClient.  "+id;
	}
	
}

//@Component
class DataSourceClientFallbackFactory implements FallbackFactory<DataSourceClient> {
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceClientFallbackFactory.class);

	@Override
	public DataSourceClient create(Throwable cause) {
		LOG.error(cause.getMessage());
		return new DataSourceClient() {
			@Override
			public String getDataSourceById(String id) {
				return "error";
			}
		};
	}
}