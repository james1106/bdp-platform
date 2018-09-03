package com.bdp.tm.manager.service;

import com.bdp.tm.model.TxServer;
import com.bdp.tm.model.TxState;

/**
 * create by lorne on 2017/11/11
 */
public interface MicroService {

    String  tmKey = "tx-manager";

    TxServer getServer();

    TxState getState();
}
