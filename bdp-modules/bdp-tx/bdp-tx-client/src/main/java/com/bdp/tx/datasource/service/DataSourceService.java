package com.bdp.tx.datasource.service;

import com.lorne.core.framework.utils.task.Task;

/**
 * 在任务超时后会执行主动查询TM的操作，根据查询结果来执行提交或回滚操作，<br/>
 * 在LCNDBConnection.transaction()的超时设置里面有调用
 * 
 * @author jack
 *
 */
public interface DataSourceService {
	/**
	 * 根据groupId查询TM服务器，根据设置结果设置waitTask状态，然后唤醒线程使事务直正的提交或者回滚
	 * 
	 * @param groupId
	 * @param waitTask
	 */
	void schedule(String groupId, Task waitTask);
}
