package com.bdp.tx.listener.service;

/**
 * 分布式服务客户端启动类，在该类中启动netty客户与服务端的连接，用做后面通信使用<br/>
 * 应该在各自应用框架或容器启动后调用该类的start()方法
 */
public interface InitService {

	void start();
}
