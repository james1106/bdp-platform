package com.bdp.tx.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdp.tx.control.service.TransactionControlService;
import com.bdp.tx.framework.utils.SocketManager;
import com.bdp.tx.listener.service.ModelNameService;
import com.bdp.tx.netty.service.MQTxManagerService;
import com.bdp.tx.netty.service.NettyControlService;
import com.bdp.tx.netty.service.NettyService;
import com.bdp.tx.netty.utils.IpAddressUtils;
import com.lorne.core.framework.utils.task.ConditionUtils;
import com.lorne.core.framework.utils.task.IBack;
import com.lorne.core.framework.utils.task.Task;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class NettyControlServiceImpl implements NettyControlService {

	@Autowired
	private NettyService nettyService;

	@Autowired
	private TransactionControlService transactionControlService;

	@Autowired
	private MQTxManagerService mqTxManagerService;

	@Autowired
	private ModelNameService modelNameService;

	@Override
	public void restart() {
		nettyService.close();
		try {
			Thread.sleep(1000 * 3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		nettyService.start();
	}

	@Override
	public void uploadModelInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!SocketManager.getInstance().isNetState()
						|| !IpAddressUtils.isIpAddress(modelNameService.getIpAddress())) {
					try {
						Thread.sleep(1000 * 5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				mqTxManagerService.uploadModelInfo();
			}
		}).start();
	}

	@Override
	public void executeService(final ChannelHandlerContext ctx, final String json) {
		if (StringUtils.isNotEmpty(json)) {
			JSONObject resObj = JSONObject.parseObject(json);
			if (resObj.containsKey("a")) {
				// a表示action,是tm发送给tx模块的处理命令。即通过a值来做事务提交或补偿操作。
				// a的取值为t或c,分别对应调用ActionTServiceImpl和ActionCServiceImpl。
				transactionControlService.notifyTransactionMsg(ctx, resObj, json);
			} else {
				// tx发送数据给tm的响应返回数据
				String key = resObj.getString("k");
				responseMsg(key, resObj);
			}
		}
	}

	private void responseMsg(String key, JSONObject resObj) {
		if (!"h".equals(key)) {// 表示非心跳数据
			// 获取具体的数据
			final String data = resObj.getString("d");
			// 通过key获取对应的Task
			Task task = ConditionUtils.getInstance().getTask(key);
			if (task != null) {
				if (task.isAwait()) {
					// 设置好接收返回消息的回调接口
					task.setBack(new IBack() {
						@Override
						public Object doing(Object... objs) throws Throwable {
							return data;
						}
					});
					// 唤醒正在等的线程，即当前响应对应的请求线程
					task.signalTask();
				}
			}
		} else {
			// 心跳数据
			final String data = resObj.getString("d");
			// 心跳正常，即设置网络状态为正常
			SocketManager.getInstance().setNetState(true);
			if (StringUtils.isNotEmpty(data)) {
				try {
					// 可知消息接收超时时间是由心跳发过来的
					SocketManager.getInstance().setDelay(Integer.parseInt(data));
				} catch (Exception e) {
					SocketManager.getInstance().setDelay(1);
				}
			}
		}
	}
}
