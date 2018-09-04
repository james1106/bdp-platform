package com.bdp.tx.netty.service;

public interface NettyService {

    void start();

    void close();

    boolean checkState();

}
