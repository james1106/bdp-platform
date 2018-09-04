package com.bdp.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdp.tx.commons.utils.task.ConditionUtils;
import com.bdp.tx.commons.utils.task.IBack;
import com.bdp.tx.commons.utils.task.Task;

public class BaseSignalTaskService {

    public String execute(String channelAddress, String key, JSONObject params) {
        String res = "";
        final String data = params.getString("d");
        Task task = ConditionUtils.getInstance().getTask(key);
        if (task != null) {
            task.setBack(new IBack() {
                @Override
                public Object doing(Object... objs) throws Throwable {
                    return data;
                }
            });
            task.signalTask();
        }
        return res;
    }
}
