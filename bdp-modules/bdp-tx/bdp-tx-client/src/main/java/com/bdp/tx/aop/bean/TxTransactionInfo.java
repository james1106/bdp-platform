package com.bdp.tx.aop.bean;

import com.bdp.tx.annotation.TxTransaction;
import com.bdp.tx.model.TransactionInvocation;


/**
 * 事务信息的封装
 */
public class TxTransactionInfo {

    private TxTransaction transaction;

    private TxTransactionLocal txTransactionLocal;

    /**
     * 事务组Id
     */
    private String txGroupId;


    private TransactionInvocation invocation;


    public TxTransactionInfo(TxTransaction transaction, TxTransactionLocal txTransactionLocal, TransactionInvocation invocation, String txGroupId) {
        this.transaction = transaction;
        this.txTransactionLocal = txTransactionLocal;
        this.txGroupId = txGroupId;
        this.invocation = invocation;
    }


    public TxTransaction getTransaction() {
        return transaction;
    }

    public TxTransactionLocal getTxTransactionLocal() {
        return txTransactionLocal;
    }

    public String getTxGroupId() {
        return txGroupId;
    }

    public TransactionInvocation getInvocation() {
        return invocation;
    }

}
