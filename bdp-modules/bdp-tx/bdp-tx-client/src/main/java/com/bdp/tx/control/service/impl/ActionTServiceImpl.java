package com.bdp.tx.control.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdp.tx.control.service.IActionService;
import com.bdp.tx.datasource.ILCNTransactionControl;
import com.bdp.tx.framework.task.TaskGroup;
import com.bdp.tx.framework.task.TaskGroupManager;
import com.bdp.tx.framework.task.TaskState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通知提交
 */
@Service(value = "t")
public class ActionTServiceImpl implements IActionService {

	private Logger logger = LoggerFactory.getLogger(ActionTServiceImpl.class);

	@Autowired
	private ILCNTransactionControl transactionControl;

	@Override
	public String execute(JSONObject resObj, String json) {
		String res;
		// 通知提醒
		final int state = resObj.getInteger("c");
		String taskId = resObj.getString("t");
		// transactionControl这是个全局变量，使用的是否有问题？
		if (transactionControl.executeTransactionOperation()) {
			TaskGroup taskGroup = TaskGroupManager.getInstance().getTaskGroup(taskId);
			logger.info("accept notify data ->" + json);
			if (taskGroup != null) {
				if (taskGroup.isAwait()) { // 已经在等待TM当前的这个命令
					res = notifyWaitTask(taskGroup, state);
				} else {
					int count = 0;
					while (true) {
						if (taskGroup.isAwait()) { // 已经在等待TM当前的这个命令
							res = notifyWaitTask(taskGroup, state);
							break;
						}
						// 自循环500次，每次会停1毫秒，即这里while最多执行0.5秒钟，
						// 在这0.5秒中等待taskGroup中的所有的TxTask都进行await状态
						// 进入这个状态才说明connection那里准备好了，正在等待TM当前的这个命令
						if (count > 500) {
							res = "0";
							break;
						}
						count++;
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				res = "0";
			}
		} else {
			// 非事务操作
			res = "1";
			transactionControl.autoNoTransactionOperation();
		}
		logger.info("accept notify response res ->" + res);
		return res;
	}

	private String notifyWaitTask(TaskGroup taskGroup, int state) {
		String res;
		taskGroup.setState(state);
		taskGroup.signalTask();
		int count = 0;
		// 很重要，这里自循环等待事务提交的结果，这个结果还会发送给TxManager的
		while (true) {
			if (taskGroup.isRemove()) {
				if (taskGroup.getState() == TaskState.rollback.getCode()
						|| taskGroup.getState() == TaskState.commit.getCode()) {
					res = "1";
				} else {
					res = "0";
				}
				break;
			}
			// 自循环1000次，每次会停1毫秒，即这里while最多执行一秒钟，
			// 在这一秒中等待taskGroup中的所有的TxTask都执行完成,已经被remove了
			// 这个很重要，这说明connection已经接收到了TM的命令，提交或者回滚已经完成，可以查看完成的最终结果了
			if (count > 1000) {
				// 已经通知了，有可能失败.
				res = "2";
				break;
			}
			count++;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
}
