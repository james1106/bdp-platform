package com.bdp.hystrix;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bdp.tx.aop.bean.TxCompensateLocal;
import com.bdp.tx.aop.bean.TxTransactionLocal;

@Component
public class TxThreadLocalRegister implements ThreadLocalShareRegister {

	@SuppressWarnings("rawtypes")
	@Override
	public List<ThreadLocal> getThreadLocals() {
		List<ThreadLocal> locals = new ArrayList<>();
		locals.add(TxTransactionLocal.currentLocal);
		locals.add(TxCompensateLocal.currentLocal);
		return locals;
	}
}
