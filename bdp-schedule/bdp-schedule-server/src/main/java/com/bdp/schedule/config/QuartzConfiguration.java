package com.bdp.schedule.config;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import com.bdp.schedule.impl.AutowiredQuartzJobFactory;

@Configuration
public class QuartzConfiguration {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PlatformTransactionManager txManager;

	@Autowired
	private AutowiredQuartzJobFactory jobFactory;

	@Bean
	public SchedulerFactoryBean schedulerFactory() throws IOException {
		SchedulerFactoryBean sfBean = new SchedulerFactoryBean();
		sfBean.setDataSource(dataSource);
		sfBean.setTransactionManager(txManager);
		sfBean.setQuartzProperties(getQuartzConfigs());
		sfBean.setAutoStartup(true);
		// 覆盖已存在定时任务
		sfBean.setOverwriteExistingJobs(true);
		sfBean.setWaitForJobsToCompleteOnShutdown(false);
		sfBean.setJobFactory(jobFactory);
		return sfBean;
	}

	private Properties getQuartzConfigs() throws IOException {
		PropertiesFactoryBean pfBean = new PropertiesFactoryBean();
		pfBean.setLocation(new ClassPathResource("quartz.properties"));
		// 在quartz.properties中的属性被读取并注入后再初始化对象
		pfBean.afterPropertiesSet();
		return pfBean.getObject();
	}

	/**
	 * 添加具体负载均衡的RestTemplate，使其可以调用eureka上注册的服务
	 * 
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
