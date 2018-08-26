package com.bdp.auth.hystrix;

import java.util.List;

public interface ThreadLocalShareRegister {
	@SuppressWarnings("rawtypes")
	public List<ThreadLocal> getThreadLocals();
}
