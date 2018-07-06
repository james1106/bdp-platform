package com.bdp.schedule;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.bdp.*.feign" })
public class ScheduleAutoConfiguration {

}
