package com.bdp.tm.api.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdp.tm.api.service.ApiTxManagerService;
import com.bdp.tm.compensate.model.TransactionCompensateMsg;
import com.bdp.tm.compensate.service.CompensateService;
import com.bdp.tm.config.ConfigReader;
import com.bdp.tm.manager.service.MicroService;
import com.bdp.tm.manager.service.TxManagerSenderService;
import com.bdp.tm.manager.service.TxManagerService;
import com.bdp.tm.model.TxServer;
import com.bdp.tm.model.TxState;

@Service
public class ApiTxManagerServiceImpl implements ApiTxManagerService {


    @Autowired
    private TxManagerService managerService;

    @Autowired
    private MicroService eurekaService;

    @Autowired
    private CompensateService compensateService;


    @Autowired
    private TxManagerSenderService txManagerSenderService;

    @Autowired
    private ConfigReader configReader;


    @Override
    public TxServer getServer() {
        return eurekaService.getServer();
    }


    @Override
    public int cleanNotifyTransaction(String groupId, String taskId) {
        return managerService.cleanNotifyTransaction(groupId,taskId);
    }


    @Override
    public boolean sendCompensateMsg(long currentTime, String groupId, String model, String address, String uniqueKey, String className, String methodStr, String data, int time,int startError) {
        TransactionCompensateMsg transactionCompensateMsg = new TransactionCompensateMsg(currentTime, groupId, model, address, uniqueKey, className, methodStr, data, time, 0,startError);
        return compensateService.saveCompensateMsg(transactionCompensateMsg);
    }

    @Override
    public String sendMsg(String model,String msg) {
        return txManagerSenderService.sendMsg(model, msg, configReader.getTransactionNettyDelayTime());
    }


    @Override
    public TxState getState() {
        return eurekaService.getState();
    }
}
