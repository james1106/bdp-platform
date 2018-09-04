package com.bdp.tx.netty.service;

import io.netty.channel.ChannelHandlerContext; 

public interface NettyControlService {
    void restart();


    void executeService(ChannelHandlerContext ctx, String json);


    void uploadModelInfo();

}
