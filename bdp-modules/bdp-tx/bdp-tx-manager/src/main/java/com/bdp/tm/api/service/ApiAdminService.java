package com.bdp.tm.api.service;

import com.bdp.tm.compensate.model.TxModel;
import com.bdp.tm.model.ModelName;
import com.bdp.tm.model.TxState;
import com.bdp.tx.commons.exception.ServiceException;

import java.util.List;

public interface ApiAdminService {

    TxState getState();

    String loadNotifyJson();

    List<ModelName> modelList();


    List<String> modelTimes(String model);

    List<TxModel> modelInfos(String path);

    boolean compensate(String path) throws ServiceException;

    boolean hasCompensate();

    boolean delCompensate(String path);

}
