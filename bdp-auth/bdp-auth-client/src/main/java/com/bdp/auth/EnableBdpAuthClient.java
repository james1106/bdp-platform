package com.bdp.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.bdp.auth.config.AuthWebConfig;
import com.bdp.auth.config.RequestHeaderConfig;
import com.bdp.auth.config.RequestSecurityConfig;

//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@ComponentScan
//@Inherited
//// 作为提供给其它应用使用的第三方JAR包，其内定义的必要的配置最好使用@import引入，
//// 否则@ConditionalOnBean这类注解同样可能找不到该配置，除非该配置只是默认的，允许用户自定义覆盖的。
////@Import({ AuthWebConfig.class, RequestSecurityConfig.class })
//@EnableResourceServer
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan
@Documented
@Inherited
@EnableResourceServer
public @interface EnableBdpAuthClient {
}