package com.bdp.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@SpringBootApplication
// 因为当前类也会被扫描到，在spring boot2.0.0以上的版本(不包括2.0)如果被扫描路径下有多个@SpringBootApplication标注的类可能报错
// 当前项目会单独开发并且也会被被其它项目依赖，所以通过该属性来标识当前@SpringBootApplication配置是否启用，便于开发测试，这是个巨坑啊
@ConditionalOnProperty(name = "bdp-platform.sys-code", havingValue = "framework")
public class FrameworkApplication {
	public static void main(String[] args) {
		SpringApplication.run(FrameworkApplication.class, args);
	}
}
