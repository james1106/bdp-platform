package com.bdp.metadata.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bdp.metadata.remoteapi.DataSourceClient;

import feign.hystrix.FallbackFactory;

@Component
public class DataSourceClientFallbackFactory implements FallbackFactory<DataSourceClient> {
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceClientFallbackFactory.class);

	@Override
	public DataSourceClient create(Throwable cause) {
		LOG.error(cause.getMessage());
		return new DataSourceClient() {
			@Override
			public String getDataSourceById(String id) {
				return "ERROR getDataSourceById from DataSourceClientFallbackFactory.[" + id + "]";
			}
		};
	}
}