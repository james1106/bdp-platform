package com.bdp.tx.listener.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdp.tx.listener.service.InitService;
import com.bdp.tx.netty.service.NettyService;

@Service
public class InitServiceImpl implements InitService {

	private final static Logger logger = LoggerFactory.getLogger(InitServiceImpl.class);

	@Autowired
	private NettyService nettyService;

	@Override
	public void start() {
		nettyService.start();
		welcome();
		logger.info("socket-start..");
	}

	private void welcome() {
		System.out.println();
		System.out.println();
		System.out.println("\t\t**  \t\t ****\t\t**  **");
		System.out.println("\t\t**  \t\t**   \t\t*** **");
		System.out.println("\t\t**  \t\t**   \t\t** ***");
		System.out.println("\t\t*****\t\t ****\t\t**  **");
		System.out.println();
		System.out.println("\t\tLCN-Client version:4.1.0");
		System.out.println();
	}

}
