package com.bdp.tx.aop.service;

import com.bdp.tx.aop.bean.TxTransactionInfo;

/**
 * 根据传入的TxTransactionInfo信息， 返回不同的TransactionServer，
 */
public interface TransactionServerFactoryService {

    TransactionServer createTransactionServer(TxTransactionInfo info) throws Throwable;
}
