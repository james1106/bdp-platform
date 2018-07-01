package com.bdp.config.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import com.bdp.config.biz.PropertyBiz;
import com.bdp.config.entity.Property;

/**
 * 
 * @author Jack
 *
 */
public class DBEnvironmentRepository implements EnvironmentRepository, Ordered {

	private PropertyBiz propertyBiz;

	private int order = Ordered.LOWEST_PRECEDENCE - 20;

	public DBEnvironmentRepository(PropertyBiz propertyBiz) {
		this.propertyBiz = propertyBiz;
	}

	@Override
	public Environment findOne(String application, String profile, String label) {
		String config = application;
		if (StringUtils.isEmpty(label)) {
			label = null;
		}
		if (StringUtils.isEmpty(profile)) {
			profile = "default";
		}
		if (!profile.startsWith("default")) {
			profile = "default," + profile;
		}
		String[] profiles = StringUtils.commaDelimitedListToStringArray(profile);
		Environment environment = new Environment(application, profiles, label, null, null);
		if (!config.startsWith("application")) {
			config = "application," + config;
		}
		List<String> applications = new ArrayList<String>(
				new LinkedHashSet<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(config))));
		List<String> envs = new ArrayList<String>(new LinkedHashSet<>(Arrays.asList(profiles)));
		Collections.reverse(applications);
		Collections.reverse(envs);
		for (String app : applications) {
			for (String env : envs) {
				Map<String, String> next = new HashMap<String, String>();
				List<Property> props = propertyBiz.queryProfile(app, env, label);
				for (Property property : props) {
					next.put(property.getProKey(), property.getProValue());
				}
				if (!next.isEmpty()) {
					environment.add(new PropertySource(app + "-" + env, next));
				}
			}
		}
		return environment;
	}

	@Override
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}