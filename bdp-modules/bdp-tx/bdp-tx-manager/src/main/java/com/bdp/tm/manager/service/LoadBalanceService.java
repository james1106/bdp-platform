package com.bdp.tm.manager.service;

import com.bdp.tm.model.LoadBalanceInfo;

public interface LoadBalanceService {

    boolean put(LoadBalanceInfo loadBalanceInfo);

    LoadBalanceInfo get(String groupId,String key);

    boolean remove(String groupId);

    String getLoadBalanceGroupName(String groupId);

}
