package com.bdp.tm.netty.service.impl;

import org.springframework.stereotype.Service;

import com.bdp.tm.netty.service.IActionService;

/**
 * 通知事务回调
 */
@Service(value = "t")
public class ActionTServiceImpl extends BaseSignalTaskService implements IActionService {

}
