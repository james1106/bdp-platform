package com.bdp.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 这是另外一种添加Basic认证的方法
 * 
 * @author jack
 *
 */
@Component
@ConditionalOnProperty(value = "zuul.accessFilter.enabled", matchIfMissing = false)
public class AccessFilter extends ZuulFilter {
	
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
		HttpServletRequest request = ctx.getRequest();
		System.out.println(request.getClass().getName());
		// 添加Basic Auth认证信息
		ctx.addZuulRequestHeader("Authorization", "Basic " + getBase64Credentials("admin", "password"));
		return null;
	}

	private String getBase64Credentials(String username, String password) {
		String plainCreds = username + ":" + password;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		return new String(base64CredsBytes);
	}

}
