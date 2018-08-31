package com.bdp.schedule.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * 用自定义JobFactory主要为了实现自动装配JOB子类有@Autowired注解的属性
 * 
 * @author JACK
 */
@Component
public class AutowiredQuartzJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

	private transient AutowireCapableBeanFactory beanFactory;

	public void setApplicationContext(ApplicationContext context) {
		beanFactory = context.getAutowireCapableBeanFactory();
	}

	@Override
	public Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object job = super.createJobInstance(bundle);
		beanFactory.autowireBean(job);
		return job;
	}
}