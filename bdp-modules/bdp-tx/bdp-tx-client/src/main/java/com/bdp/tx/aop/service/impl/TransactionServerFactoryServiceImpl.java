package com.bdp.tx.aop.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdp.tx.aop.bean.TxTransactionInfo;
import com.bdp.tx.aop.service.TransactionServer;
import com.bdp.tx.aop.service.TransactionServerFactoryService;
import com.bdp.tx.datasource.ILCNTransactionControl;
import com.bdp.tx.netty.service.NettyService;

/**
 * 根据传入的TxTransactionInfo信息， 返回不同的TransactionServer，
 */
@Service
public class TransactionServerFactoryServiceImpl implements TransactionServerFactoryService {

	@Autowired
	private TransactionServer txStartTransactionServer;

	@Autowired
	private TransactionServer txDoTransactionServer;

	@Autowired
	private TransactionServer txInTransactionServer;

	@Autowired
	private TransactionServer txNoTransactionServer;

	@Autowired
	private NettyService nettyService;

	@Autowired
	private ILCNTransactionControl transactionControl;

	/**
	 * 根据传入的TxTransactionInfo信息， 返回不同的TransactionServer，
	 */
	public TransactionServer createTransactionServer(TxTransactionInfo info) throws Throwable {

		/********* 分布式事务处理逻辑*开始 ***********/
		/** 仅当Transaction注解不为空，其他都为空时。表示分布式事务开始启动 **/
		if (info.getTransaction() != null && info.getTransaction().isStart() && info.getTxTransactionLocal() == null
				&& StringUtils.isEmpty(info.getTxGroupId())) {
			// 检查socket通讯是否正常
			// （当启动事务的主业务方法执行完以后，再执行当前系统的其他业务方法时将进入txInServiceTransactionServer做业务处理）
			if (nettyService.checkState()) {
				return txStartTransactionServer;
			} else {
				throw new Exception("tx-manager not connected ,please check tx-manager server ");
			}
		}

		/** 分布式事务已经开启，业务进行中 **/
		if (info.getTxTransactionLocal() != null || StringUtils.isNotEmpty(info.getTxGroupId())) {
			// 检查socket通讯是否正常
			// （第一次执行时启动txRunningTransactionServer的业务处理控制，然后嵌套调用当前模块的其他事务的业务方法时都并到txInServiceTransactionServer业务处理下）
			if (nettyService.checkState()) {
				if (info.getTxTransactionLocal() != null) {
					return txInTransactionServer;
				} else {
					// 有事务的业务操作
					if (!transactionControl.isNoTransactionOperation()) { 
						return txDoTransactionServer;
					} else {
						return txNoTransactionServer;
					}
				}
			} else {
				throw new Exception("tx-manager not connected ,please check tx-manager server ");
			}
		}
		/********* 分布式事务处理逻辑*结束 ***********/
		return txInTransactionServer;
	}
}
