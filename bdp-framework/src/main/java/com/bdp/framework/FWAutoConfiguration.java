package com.bdp.framework;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
//PropertySource只支持properties文件，不支持yml
@PropertySource("classpath:/bdp-defaults.properties")
public class FWAutoConfiguration {

}
