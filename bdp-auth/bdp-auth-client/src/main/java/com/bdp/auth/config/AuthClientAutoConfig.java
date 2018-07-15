package com.bdp.auth.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@ComponentScan("com.bdp.auth")
@EnableResourceServer
public class AuthClientAutoConfig {

}
