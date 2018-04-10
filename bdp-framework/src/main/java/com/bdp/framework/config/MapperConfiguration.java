package com.bdp.framework.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * mybatis mapper 扫描配置类
 */
@Configuration
@AutoConfigureAfter(MybatisConfiguration.class)
public class MapperConfiguration {

	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(Environment environment) {
		RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment);
		String basePackage = resolver.getProperty("mybatis.basepackage");
		if (StringUtils.isEmpty(basePackage)) {
			basePackage = "com.bdp.**.mapper";
		}
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		mapperScannerConfigurer.setBasePackage(basePackage);
		return mapperScannerConfigurer;
	}
}