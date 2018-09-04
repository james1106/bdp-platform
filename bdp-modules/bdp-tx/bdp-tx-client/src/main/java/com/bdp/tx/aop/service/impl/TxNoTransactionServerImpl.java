package com.bdp.tx.aop.service.impl;

import com.bdp.tx.Constants;
import com.bdp.tx.aop.bean.TxTransactionInfo;
import com.bdp.tx.aop.bean.TxTransactionLocal;
import com.bdp.tx.aop.service.TransactionServer;
import com.bdp.tx.commons.utils.KidUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 分布式事务启动参与事务中的业务处理（无事务模块）
 */
@Service(value = "txNoTransactionServer")
public class TxNoTransactionServerImpl implements TransactionServer {


    private Logger logger = LoggerFactory.getLogger(TxNoTransactionServerImpl.class);

    @Override
    public Object execute(final ProceedingJoinPoint point, final TxTransactionInfo info) throws Throwable {

        String kid = KidUtils.generateShortUuid();
        String txGroupId = info.getTxGroupId();
        logger.debug("--->begin no db transaction, groupId: " + txGroupId);
        long t1 = System.currentTimeMillis();


        TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
        txTransactionLocal.setGroupId(txGroupId);
        txTransactionLocal.setHasStart(false);
        txTransactionLocal.setKid(kid);
        txTransactionLocal.setMaxTimeOut(Constants.txServer.getCompensateMaxWaitTime());
        TxTransactionLocal.setCurrent(txTransactionLocal);

        try {
            return point.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            TxTransactionLocal.setCurrent(null);
            long t2 = System.currentTimeMillis();
            logger.debug("<---end no db transaction,groupId:" + txGroupId+",execute time:"+(t2-t1));
        }
    }

}
