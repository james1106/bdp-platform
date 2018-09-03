package com.bdp.tx;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.bdp.hystrix.HystrixConcurrencyStrategyConfig;
import com.bdp.ribbon.loadbalancer.LcnRibbonConfiguration;

@Configuration
@ComponentScan
@RibbonClients(defaultConfiguration = LcnRibbonConfiguration.class)
@Import(HystrixConcurrencyStrategyConfig.class)
public class TransactionConfiguration {

}
