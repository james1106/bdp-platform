package com.bdp.framework.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

// 通过指定 basePackages，保证framework的包内配置都会生效，不指定再从当前类的目录开始扫描
// 那样就会导致如果当前项目被其它项目依赖，则不能扫描到当前项目其它包下面的配置了。
@ComponentScan("com.bdp.framework")
// 导入自定义的配置文件，目前只支持properties，不支持yml文件
@PropertySource("classpath:bdp-defaults.properties")
// 显示指定，否则其它项目依赖时会找不到实体类的映射，这里用了路径匹配，保证匹配到的entity包都能被扫描到
// 否则依赖当前framework的其它项目就会找不到映射。如果实体类不在匹配的路径下可以再通过当前注解指定路径
@EntityScan("com.bdp.*.entity")
public class BdpAutoConfiguration {

}
