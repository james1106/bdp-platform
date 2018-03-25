package com.bdp.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@EnableWebSecurity
public class BdpSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService bdpUserDetailsService() {
		return new BdpUserDetailsService();
	}

	/**
	 * 将HttpSecurity 公开备用
	 * 
	 * @return
	 * @throws Exception
	 */
	public HttpSecurity httpSecurity() throws Exception {
		return getHttp();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 表示所有的访问都必须进行认证请求处理后才能正常进行
		http.httpBasic().and().authorizeRequests().anyRequest().fullyAuthenticated();
		// 设置session为无状态,提升操作效率
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Autowired
	public void configGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(bdpUserDetailsService());
	}

	class BdpUserDetailsService implements UserDetailsService {

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("adminstrator"));
			return new User("admin", "password", authorities);
		}
	}
}
