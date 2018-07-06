package com.bdp.auth.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.bdp.auth.constant.AuthConstant;
import com.bdp.auth.exception.ValidateCodeException;

@Component
public class BdpAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			String msg_key = AuthConstant.AUTHENTICATION_FAILURE_MSG;
			if (exception instanceof BadCredentialsException) {
				session.setAttribute(msg_key, "密码错误！");
			} else if (exception instanceof UsernameNotFoundException) {
				session.setAttribute(msg_key, "用户名不存在！");
			} else if (exception instanceof ValidateCodeException) {
				session.setAttribute(msg_key, exception.getMessage());
			} else {
				session.setAttribute(msg_key, "登录失败，请联系管理员！");
			}
		}
		super.onAuthenticationFailure(request, response, exception);
	}

}
