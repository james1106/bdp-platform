package com.bdp.auth.vcode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bdp.auth.exception.ValidateCodeException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 图形验证码和短信验证码过滤器
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
	/**
	 * 验证码校验失败处理器
	 */
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	private ValidateCodeRepository repository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			// 验证登录表单的请求
			if ("/loginForm".equals(request.getRequestURI())) {
				logger.info("校验请求(" + request.getRequestURI() + ")中的验证码");
				String vcode = request.getParameter("vcode");
				if (StringUtils.isEmpty(vcode)) {
					throw new ValidateCodeException("图形验证码不能为空");
				}
				ValidateCode validateCode = repository.get(request);
				if (validateCode == null) {
					throw new ValidateCodeException("图形验证码验证失败");
				}
				if (validateCode.isExpried()) {
					throw new ValidateCodeException("图形验证码已经过期");
				}
				if (!StringUtils.equals(vcode, validateCode.getCode())) {
					throw new ValidateCodeException("图形验证码输出错误");
				}
				logger.info("验证码校验通过");
			}
		} catch (AuthenticationException exception) {
			authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
			return;
		}
		chain.doFilter(request, response);
	}
}
