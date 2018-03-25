package com.bdp.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.discovery.DiscoveryClient;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

@Configuration
public class EurekaSecurityConfiguration {

	@Bean
	public DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs() {
		DiscoveryClient.DiscoveryClientOptionalArgs args = new DiscoveryClient.DiscoveryClientOptionalArgs();
		List<ClientFilter> filters = new ArrayList<>();
		filters.add(new HTTPBasicAuthFilter("admin", "password"));
		args.setAdditionalFilters(filters);
		return args;
	}
}
