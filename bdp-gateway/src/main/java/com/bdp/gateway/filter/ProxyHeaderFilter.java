package com.bdp.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 把路由中的映射路径添加到请求头中传递给下游应用。<br/>
 * 下游应用会根据该参数进行资源请求的绝对路径设置。
 * @author jack
 *
 */
@Component
public class ProxyHeaderFilter extends ZuulFilter {

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
		String zuulProxy="";
		String uri=request.getRequestURI();
		int second=uri.indexOf("/", 1);
		if(second>0) {
			zuulProxy=uri.substring(1, second);
		}else {
			zuulProxy=uri.substring(1);
		}
		ctx.addZuulRequestHeader("zuulProxy", zuulProxy);
		return ctx;
	}
}