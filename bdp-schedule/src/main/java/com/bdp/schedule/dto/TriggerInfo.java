package com.bdp.schedule.dto;

import java.io.Serializable;

/**
 * 封装的是触发器相关的信息
 * 
 * @author JACK
 *
 */
public class TriggerInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TRIGGER_TYPE_DAY = "day";
	public static final String TRIGGER_TYPE_WEEK = "week";
	public static final String TRIGGER_TYPE_MONTH = "month";
	public static final String TRIGGER_TYPE_CRON = "cron";

	// 触发类型,即按日、按周、按月、cron自定义等
	private String triggerType;

	/**
	 * 触发类型，可选值见该类型定义的常量TRIGGER_TYPE_*常量
	 * 
	 * @return
	 */
	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
}
