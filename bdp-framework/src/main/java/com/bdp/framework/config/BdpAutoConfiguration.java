package com.bdp.framework.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
// 通过指定 basePackages，保证framework的包内配置都会生效，不指定再从当前类的目录开始扫描
// 那样就会导致如果当前项目被其它项目依赖，则不能扫描到当前项目其它包下面的配置了。
@ComponentScan("com.bdp.framework")
// 导入自定义的配置文件，目前只支持properties，不支持yml文件
@PropertySource("classpath:bdp-defaults.properties")
public class BdpAutoConfiguration {

}
