package com.bdp.tx.control.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

/**
 * 事务控制服务，主要是接收TM指令，并返回指令执行结果，<br/>
 * 其在NettyControlServiceImpl.executeService被调用
 */
public interface TransactionControlService {

	void notifyTransactionMsg(ChannelHandlerContext ctx, JSONObject resObj, String json);

}
