package com.bdp.metadata.fallback;

import org.springframework.stereotype.Component;

import com.bdp.metadata.remoteapi.DataSourceClient;

import feign.hystrix.FallbackFactory;

@Component
public class DataSourceClientFallbackFactory implements FallbackFactory<DataSourceClient> {

	@Override
	public DataSourceClient create(Throwable cause) {
		cause.printStackTrace();
		return new DataSourceClient() {
			@Override
			public String getDataSourceById(String id) {
				return "ERROR getDataSourceById from DataSourceClientFallbackFactory.[" + id + "]";
			}
		};
	}
}