package com.bdp.etl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.bdp.auth.EnableBdpAuthClient;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableBdpAuthClient
public class EtlServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EtlServerApplication.class, args);
	}
}
