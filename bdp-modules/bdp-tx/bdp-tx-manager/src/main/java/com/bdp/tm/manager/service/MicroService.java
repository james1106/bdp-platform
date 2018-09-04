package com.bdp.tm.manager.service;

import com.bdp.tm.model.TxServer;
import com.bdp.tm.model.TxState;

public interface MicroService {

    String  tmKey = "tx-manager";

    TxServer getServer();

    TxState getState();
}
