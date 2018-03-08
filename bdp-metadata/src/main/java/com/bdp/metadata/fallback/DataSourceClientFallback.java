package com.bdp.metadata.fallback;

import org.springframework.stereotype.Component;

import com.bdp.metadata.remoteapi.DataSourceClient;

@Component
public class DataSourceClientFallback implements DataSourceClient {

	@Override
	public String getDataSourceById(String id) {
		return "ERROR getDataSourceById from DataSourceClientFallback.[" + id + "]";
	}

}