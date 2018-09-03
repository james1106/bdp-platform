package com.bdp.tx.springcloud.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bdp.tx.aop.tx.TxGroupInterceptor;

import javax.servlet.http.HttpServletRequest;

/**
 * Springcloud Http请求时获取事务组ID的实现类
 * 
 * @author jack
 *
 */
@Component
public class HttpReqTxGroupInterceptor implements TxGroupInterceptor {
	public String obtainTxGroupId() throws Throwable {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = requestAttributes == null ? null
				: ((ServletRequestAttributes) requestAttributes).getRequest();
		return request == null ? null : request.getHeader("tx-group");
	}
}
