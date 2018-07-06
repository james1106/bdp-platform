package com.bdp.auth.vcode.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.bdp.auth.vcode.ValidateCode;
import com.bdp.auth.vcode.ValidateCodeRepository;

/**
 * 基于session的验证码存取器
 */
@Component
public class SessionValidateCodeRepository implements ValidateCodeRepository {

	/**
	 * 验证码放入session中的键值
	 */
	String VALIDATECODE_SESSION_KEY = "VALIDATECODE_SESSION_KEY";

	@Override
	public void save(HttpServletRequest request, ValidateCode code) {
		HttpSession session = request.getSession();
		session.setAttribute(VALIDATECODE_SESSION_KEY, code);
	}

	@Override
	public ValidateCode get(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (ValidateCode) session.getAttribute(VALIDATECODE_SESSION_KEY);
	}

	@Override
	public void remove(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(VALIDATECODE_SESSION_KEY);
	}
}