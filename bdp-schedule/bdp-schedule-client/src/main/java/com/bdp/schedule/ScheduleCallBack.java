package com.bdp.schedule;

import com.bdp.schedule.dto.ScheduleInfo;

public interface ScheduleCallBack {
	/**
	 * 执行指定的调度任务
	 * 
	 * @param info
	 *            调度信息
	 * @throws Exception
	 */
	public void execute(ScheduleInfo info) throws Exception;

	/**
	 * 判断当前回调的实现类是否支持当前调度的执行
	 * 
	 * @param callBack
	 *            回调字符串
	 * @return
	 */
	public boolean isSupport(String callBack);
}
