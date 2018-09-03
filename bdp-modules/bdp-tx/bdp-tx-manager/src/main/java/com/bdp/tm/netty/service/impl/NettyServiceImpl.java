package com.bdp.tm.netty.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.bdp.tm.netty.service.IActionService;
import com.bdp.tm.netty.service.NettyService;

@Service
public class NettyServiceImpl implements NettyService{

    @Autowired
    private ApplicationContext spring;

    @Override
    public IActionService getActionService(String action) {
        return spring.getBean(action,IActionService.class);
    }
}
