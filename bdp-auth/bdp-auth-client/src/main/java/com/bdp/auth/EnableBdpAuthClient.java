package com.bdp.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import com.bdp.auth.config.AuthClientAutoConfig;
import com.bdp.auth.hystrix.HystrixConcurrencyStrategyConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ AuthClientAutoConfig.class, HystrixConcurrencyStrategyConfig.class })
// 注意：不能直接用下面这两个代替@Import(AuthClientAutoConfig.class)，从而省掉AuthClientAutoConfig类，
// 因为@ComponentScan添加到类上与添加到其他注解上作用不同，添加到其它注解上面好像不起作用，原因暂时还没搞明白。<br/>
// 基本所有SpringBoot中的@EnableXxx的注解定义格式与当前注解一样。在做共用的JAR时，尽量不要使用@ComponentScan的注解来做扫描，
// 用@Import及各种Conditional来完成依赖的注入，参考SpringBoot的其它@EnableXxx的实现
// @ComponentScan
// @EnableResourceServer
public @interface EnableBdpAuthClient {
}