package com.bdp.auth;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@ComponentScan
@EnableResourceServer
// 很重要，注意看@ConditionalOnBean的解释，因为当前bdp-auth-client被其它项目引用时也通过spring.factories自动配置,<br/>
// 与OAuth2AutoConfiguration配置是一样方式，而OAuth2AutoConfiguration引入的OAuth2ResourceServerConfiguration依赖ResourceServerConfiguration的存在。<br/>
// 而ResourceServerConfiguration又是通过@EnableResourceServer引入的，所以当前的自动配置应该在OAuth2AutoConfiguration的自动配置之前执行，<br/>
// 否则OAuth2ResourceServerConfiguration不生效会导致@EnableResourceServer最终配置错误。<br/>
// 原则上通过Auto-configuration自动配置依赖的bean不要由其它Auto-configuration包来提供。因为两个自动配置的执行顺序不能确定。而应该由用户自定义bean来提供，<br/>
// 注意此处Auto-configuration配置特指通过spring.factories加载的配置，这种是"真的"自动配置，通过@EnableResourceServer类似的配置不是人为干预了，不是"真的"自动配置。<br/>
// 所谓用户自定义的bean,即通过当前springboot应用扫描配置能够扫描到的bean,而不是通过spring.factories这种方式配置的bean.<br/>
// @ConditionalOnBean只会查找当前执行时已经放到applicationContext中的bean,此时其它的Auto-configuration可能还没有执行。<br/>
// 另外@AutoConfigureAfter这样的注解也只是应用在Auto-configuration配置类之间的<br/>
// 所有的Auto-configuration的执行会在户自定义的bean配置完成后开始执行,当然如果用户自定义的bean需要注入Auto-configuration中提供的bean时spring会先配置依赖bean。<br/>
@AutoConfigureBefore(OAuth2AutoConfiguration.class)
public class AuthClientAutoConfiguration {

}
