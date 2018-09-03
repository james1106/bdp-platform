package com.bdp.tm.netty.service;

/**
 * 根据传入的action获取相应的处理类，被TxCoreServerHandler调用
 * 
 * @author jack
 *
 */
public interface NettyService {

	IActionService getActionService(String action);
}
