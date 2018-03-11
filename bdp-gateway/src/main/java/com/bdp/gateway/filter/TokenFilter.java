package com.bdp.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * zuul 过滤器的使用，这里演示了一个自动配置@ConditionalOnProperty注解的使用。
 * 即自定义了一个zuul.tokenfilter.enabled属性，如果为true,则启用该过滤器，否则不启用
 * @author jack
 *
 */
@Component
@ConditionalOnProperty(value = "zuul.tokenfilter.enabled", matchIfMissing = false)
public class TokenFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(TokenFilter.class);

	@Override
	public String filterType() {
		return "pre";
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
		Object accessToken = request.getParameter("token");
		if (accessToken == null) {
			log.warn("token is empty");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			try {
				ctx.getResponse().getWriter().write("token is empty");
			} catch (Exception e) {
			}
			return null;
		}
		log.info("ok");
		return null;
	}
}