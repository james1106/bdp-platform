package com.bdp.metadata.fallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.bdp.metadata.remoteapi.DataSourceClient;

@Component
public class DataSourceClientFallback implements DataSourceClient {

	private final Log LOG = LogFactory.getLog(DataSourceClientFallback.class);

	@Override
	public String getDataSourceById(String id) {
		LOG.info("Test Log level");
		return "ERROR getDataSourceById from DataSourceClientFallback.[" + id + "]";
	}

}