package com.bdp.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bdp.auth.client")
public class AuthClientProperties {

}
