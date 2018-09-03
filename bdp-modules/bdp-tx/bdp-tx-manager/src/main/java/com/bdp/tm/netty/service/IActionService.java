package com.bdp.tm.netty.service;

import com.alibaba.fastjson.JSONObject;


public interface IActionService {

    String execute(String channelAddress,String key,JSONObject params);

}
