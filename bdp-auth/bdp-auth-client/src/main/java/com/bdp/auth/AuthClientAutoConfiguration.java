package com.bdp.auth;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.bdp.auth.properties.AuthClientProperties;

@Configuration
@Import({ AuthClientProperties.class })
@ComponentScan({"com.bdp.auth"})
public class AuthClientAutoConfiguration {

}
