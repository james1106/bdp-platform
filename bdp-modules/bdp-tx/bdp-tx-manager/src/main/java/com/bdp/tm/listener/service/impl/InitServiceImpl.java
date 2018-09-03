package com.bdp.tm.listener.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdp.tm.Constants;
import com.bdp.tm.config.ConfigReader;
import com.bdp.tm.listener.service.InitService;
import com.bdp.tm.netty.service.NettyServerService;


@Service
public class InitServiceImpl implements InitService {

    @Autowired
    private NettyServerService nettyServerService;

    @Autowired
    private ConfigReader configReader;


    @Override
    public void start() {
        /**加载本地服务信息**/
        Constants.socketPort = configReader.getSocketPort();
        Constants.maxConnection = configReader.getSocketMaxConnection();
        nettyServerService.start();
        welcome();
    }


    private void welcome(){
        System.out.println();
        System.out.println();
        System.out.println("\t\t**  \t\t ****\t\t**  **");
        System.out.println("\t\t**  \t\t**   \t\t*** **");
        System.out.println("\t\t**  \t\t**   \t\t** ***");
        System.out.println("\t\t*****\t\t ****\t\t**  **");
        System.out.println();
        System.out.println("\t\tLCN-TxManager  version:4.1.0");
        System.out.println();
    }


    @Override
    public void close() {
        nettyServerService.close();
    }
}
