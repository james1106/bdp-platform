package com.bdp.tx.control.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdp.tx.control.service.IActionService;
import com.bdp.tx.control.service.TransactionControlService;
import com.bdp.tx.framework.utils.SocketUtils;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 事务控制服务，主要是接收TM指令，并返回指令执行结果，<br/>
 * 其在NettyControlServiceImpl.executeService被调用
 */
@Service
public class TransactionControlServiceImpl implements TransactionControlService {

	private Logger logger = LoggerFactory.getLogger(TransactionControlServiceImpl.class);

	@Autowired
	private ApplicationContext spring;

	@Override
	public void notifyTransactionMsg(ChannelHandlerContext ctx, JSONObject resObj, String json) {

		// a为action的缩写，取值为t或者c 分别表示事务提交通知或者事务补偿通知
		String action = resObj.getString("a");

		// k为事务组键值
		String key = resObj.getString("k");

		IActionService actionService = spring.getBean(action, IActionService.class);

		// 进行提交或者补偿操作，并获取结果
		String res = actionService.execute(resObj, json);

		JSONObject data = new JSONObject();
		data.put("k", key);
		data.put("a", action);

		JSONObject params = new JSONObject();
		params.put("d", res);
		data.put("p", params);

		// 把执行结果再次发送给tm
		SocketUtils.sendMsg(ctx, data.toString());

		logger.debug("send notify data ->" + data.toString());
	}
}
