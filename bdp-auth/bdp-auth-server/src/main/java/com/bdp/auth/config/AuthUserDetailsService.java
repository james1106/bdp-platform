package com.bdp.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bdp.framework.data.biz.impl.UserBiz;

@Component
public class AuthUserDetailsService implements UserDetailsService {

	@Autowired
	private UserBiz userBiz;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.bdp.framework.data.entity.User u = userBiz.readByName(username);
		if (u != null) {
			return new User(username, passwordEncoder.encode(u.getPassword()),
					AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
		}
		// 此处不能返回空
		throw new UsernameNotFoundException("用户名不存在");
	}

}