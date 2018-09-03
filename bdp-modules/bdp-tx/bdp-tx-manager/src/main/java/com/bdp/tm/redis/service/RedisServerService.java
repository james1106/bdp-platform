package com.bdp.tm.redis.service;

import java.util.List;

import com.bdp.tm.netty.model.TxGroup;

/**
 * create by lorne on 2017/11/11
 */
public interface RedisServerService {

    String loadNotifyJson();

    void saveTransaction(String key, String json);

    TxGroup getTxGroupByKey(String key);

    void saveCompensateMsg(String name, String json);

    List<String> getKeys(String key);

    List<String> getValuesByKeys(List<String> keys);

    String getValueByKey(String key);

    void deleteKey(String key);

    void saveLoadBalance(String groupName,String key,String data);


    String getLoadBalance(String groupName,String key);


}
