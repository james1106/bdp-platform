package com.bdp.tx.compensate.service;


import com.bdp.tx.compensate.model.CompensateInfo;
import com.bdp.tx.model.TransactionInvocation;

public interface CompensateService {


    void saveLocal(CompensateInfo compensateInfo);

    boolean invoke(TransactionInvocation invocation, String groupId, int startState);

}
