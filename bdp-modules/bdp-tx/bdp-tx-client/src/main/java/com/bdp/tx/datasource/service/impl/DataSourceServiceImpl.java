package com.bdp.tx.datasource.service.impl;

import com.bdp.tx.commons.utils.task.Task;
import com.bdp.tx.datasource.service.DataSourceService;
import com.bdp.tx.netty.service.MQTxManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataSourceServiceImpl implements DataSourceService {


    @Autowired
    private MQTxManagerService txManagerService;


    @Override
    public void schedule(String groupId, Task waitTask) {
        String waitTaskId = waitTask.getKey();
        int rs = txManagerService.cleanNotifyTransaction(groupId, waitTaskId);
        if (rs == 1 || rs == 0) {
            waitTask.setState(rs);
            waitTask.signalTask();
            return;
        }
        rs = txManagerService.cleanNotifyTransactionHttp(groupId, waitTaskId);
        if (rs == 1 || rs == 0) {
            waitTask.setState(rs);
            waitTask.signalTask();
            return;
        }
        //添加到补偿队列
        waitTask.setState(-100);
        waitTask.signalTask();
    }
}
