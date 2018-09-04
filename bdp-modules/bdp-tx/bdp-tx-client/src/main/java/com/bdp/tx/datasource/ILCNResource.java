package com.bdp.tx.datasource;

import com.bdp.tx.framework.task.TxTask;

/**
 * LCN 资源连接对象
 */
public interface ILCNResource<T> {


    /**
     * 用于关闭时检查是否未删除
     * @return TxTask任务对象
     */
    TxTask getWaitTask();

    /**
     * 事务组id
     * @return  事务组Id
     */
    String getGroupId();



}
