package com.bdp.schedule.trigger;

import org.quartz.CronTrigger;

import com.bdp.schedule.dto.TriggerInfo;

public interface TriggerCreator {

	public CronTrigger createTrigger(TriggerInfo triggerInfo) throws Exception;

	/**
	 * 根据传入的triggerInfo判断当前生成器是否支持
	 * 
	 * @return
	 */
	public boolean isSupport(TriggerInfo triggerInfo);
}
