package com.bdp.gateway.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 把当前登录用户的JWT值通过请求头传递给下游应用,<br/>
 * 使下游资源服务器可以认证通过
 * 
 * @author jack
 *
 */
@Component
public class TokenFilter extends ZuulFilter {

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof OAuth2Authentication) {
			Object details = authentication.getDetails();
			if (details != null && details instanceof OAuth2AuthenticationDetails) {
				String tokeyType = ((OAuth2AuthenticationDetails) details).getTokenType();
				String tokenValue = ((OAuth2AuthenticationDetails) details).getTokenValue();
				ctx.addZuulRequestHeader("Authorization", tokeyType + " " + tokenValue);
			}
		}
		return authentication;
	}
}