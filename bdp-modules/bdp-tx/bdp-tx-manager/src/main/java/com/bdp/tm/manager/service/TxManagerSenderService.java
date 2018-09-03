package com.bdp.tm.manager.service;

import com.bdp.tm.netty.model.TxGroup;

/**
 * tm 核心类，会综合事务发起方及参与方提交的状态确认最终事务是提交还是回滚或者补偿
 * 
 * @author jack
 *
 */
public interface TxManagerSenderService {

	/**
	 * 确认事务组，最核心方法
	 * 
	 * @param group
	 * @return
	 */
	int confirm(TxGroup group);

	String sendMsg(String model, String msg, int delay);

	String sendCompensateMsg(String model, String groupId, String data, int startState);
}
