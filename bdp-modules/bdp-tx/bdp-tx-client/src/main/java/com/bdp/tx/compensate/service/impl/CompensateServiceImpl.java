package com.bdp.tx.compensate.service.impl;

import com.alibaba.fastjson.JSON;
import com.bdp.tx.aop.bean.TxCompensateLocal;
import com.bdp.tx.compensate.model.CompensateInfo;
import com.bdp.tx.compensate.service.CompensateService;
import com.bdp.tx.framework.utils.MethodUtils;
import com.bdp.tx.model.TransactionInvocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class CompensateServiceImpl implements CompensateService {


    @Autowired
    private ApplicationContext spring;


    private Logger logger = LoggerFactory.getLogger(CompensateServiceImpl.class);

    @Override
    public void saveLocal(CompensateInfo compensateInfo) {
        String json = JSON.toJSONString(compensateInfo);
        logger.info("local-compensate-logs->" + json);
    }

    @Override
    public boolean invoke(TransactionInvocation invocation, String groupId, int startState) {

        TxCompensateLocal compensateLocal = new TxCompensateLocal();
        compensateLocal.setGroupId(groupId);
        compensateLocal.setStartState(startState);

        TxCompensateLocal.setCurrent(compensateLocal);

        boolean res = MethodUtils.invoke(spring, invocation);

        TxCompensateLocal.setCurrent(null);

        return res;
    }
}
