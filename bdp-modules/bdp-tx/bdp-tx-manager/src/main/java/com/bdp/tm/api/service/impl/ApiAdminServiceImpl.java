package com.bdp.tm.api.service.impl;

import com.bdp.tm.api.service.ApiAdminService;
import com.bdp.tm.compensate.model.TxModel;
import com.bdp.tm.compensate.service.CompensateService;
import com.bdp.tm.manager.service.MicroService;
import com.bdp.tm.model.ModelName;
import com.bdp.tm.model.TxState;
import com.bdp.tm.redis.service.RedisServerService;
import com.lorne.core.framework.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by lorne on 2017/11/12
 */
@Service
public class ApiAdminServiceImpl implements ApiAdminService {


    @Autowired
    private MicroService eurekaService;

    @Autowired
    private RedisServerService redisServerService;

    @Autowired
    private CompensateService compensateService;

    @Override
    public TxState getState() {
        return eurekaService.getState();
    }

    @Override
    public String loadNotifyJson() {
        return redisServerService.loadNotifyJson();
    }

    @Override
    public List<ModelName> modelList() {
        return compensateService.loadModelList();
    }


    @Override
    public List<String> modelTimes(String model) {
        return compensateService.loadCompensateTimes(model);
    }

    @Override
    public List<TxModel> modelInfos(String path) {
        return compensateService.loadCompensateByModelAndTime(path);
    }

    @Override
    public boolean compensate(String path) throws ServiceException {
        return compensateService.executeCompensate(path);
    }

    @Override
    public boolean delCompensate(String path) {
        return compensateService.delCompensate(path);
    }

    @Override
    public boolean hasCompensate() {
        return compensateService.hasCompensate();
    }
}
