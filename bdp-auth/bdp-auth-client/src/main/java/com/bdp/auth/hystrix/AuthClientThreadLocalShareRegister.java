package com.bdp.auth.hystrix;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bdp.auth.context.AuthContextHandler;

@Component
public class AuthClientThreadLocalShareRegister implements ThreadLocalShareRegister {

	@SuppressWarnings("rawtypes")
	@Override
	public List<ThreadLocal> getThreadLocals() {
		List<ThreadLocal> locals = new ArrayList<>();
		locals.add(AuthContextHandler.threadLocal);
		return locals;
	}

}
