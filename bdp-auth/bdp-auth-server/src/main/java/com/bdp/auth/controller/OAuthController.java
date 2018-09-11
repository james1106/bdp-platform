package com.bdp.auth.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class OAuthController {

	private SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	/**
	 * 退出登录，由sso客户端调用，通过该接口清除对应浏览器cookie中保存<br/>
	 * 在session中用户的登录信息,真正从AuthorizationServer退出登录
	 * 
	 * @param request
	 * @param response
	 * @param auth
	 */
	@GetMapping("oauth/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
		logoutHandler.logout(request, response, auth);
		try {
			// 跳转回来源页面
			response.sendRedirect(request.getHeader("referer"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
