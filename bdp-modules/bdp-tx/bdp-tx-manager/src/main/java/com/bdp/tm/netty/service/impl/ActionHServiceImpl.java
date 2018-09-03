package com.bdp.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdp.tm.config.ConfigReader;
import com.bdp.tm.netty.service.IActionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 心跳包
 */
@Service(value = "h")
public class ActionHServiceImpl implements IActionService{


    @Autowired
    private ConfigReader configReader;

    @Override
    public String execute(String channelAddress, String key, JSONObject params ) {
        return String.valueOf(configReader.getTransactionNettyDelayTime());
    }
}
