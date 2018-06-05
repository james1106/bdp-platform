package com.bdp.datasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableOAuth2Sso
@EnableResourceServer
public class DataSourceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DataSourceApplication.class, args);
	}
}
