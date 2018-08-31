package com.bdp.schedule.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	public static final String TRIGGER_TYPE_CYCLE = "cycle";
	public static final String TRIGGER_TYPE_CRON = "cron";

	public static final String EXPIRE_TYPE_NONE = "none";
	public static final String EXPIRE_TYPE_DATE = "date";
	public static final String EXPIRE_TYPE_TIMES = "times";

	// 调度名称
	private String triggerName;

	// 过期方式，即按日期、按次数，不过期
	private String expireType = EXPIRE_TYPE_NONE;

	// 在指定日期后过期
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date expireDate;

	// 执行多少次后过期
	private int expireTimes;

	// 当前调度是否生效
	private boolean validate = true;

	// 触发类型,即按日、按周、按月、循环、cron自定义等
	private String triggerType;

	// 按日执行，每天执行的具体时间
	private String dayRunTime;

	// 按周执行，具体在每周的哪几天执行
	private int[] weekRunDays;

	// 按周执行，执行的具体时间
	private String weekRunTime;

	// 按月执行，具体在哪几个月执行
	private int[] monthRunMonths;

	// 按月执行，每个月的哪一天执行
	private String monthRunDay;

	// 按月执行，每天的执行时间点
	private String monthRunTime;

	// 循环执行，的开始时间
	private String cycleTime;

	// 循环执行的时间间隔
	private int cycleInterval;

	// 自定义执行的cron表达式
	private String selfCron;

	public String getTriggerName() {
		return triggerName;
	}

	/**
	 * 触发器名称
	 * 
	 * @return
	 */
	void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getExpireType() {
		return expireType;
	}

	/**
	 * 调度过期类型，可选值见该类型定义的常量EXPIRE_TYPE_*常量
	 * 
	 * @return
	 */
	public void setExpireType(String expireType) {
		this.expireType = expireType;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * 如果过期类型为EXPIRE_TYPE_DATE时，该字段指定过期日期
	 * 
	 * @return
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public int getExpireTimes() {
		return expireTimes;
	}

	/**
	 * 如果过期类型为EXPIRE_TYPE_TIMES时，该字段指定过期次数
	 * 
	 * @return
	 */
	public void setExpireTimes(int expireTimes) {
		this.expireTimes = expireTimes;
	}

	public boolean isValidate() {
		return validate;
	}

	/**
	 * 当前调度是否立即生效
	 * 
	 * @return
	 */
	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getTriggerType() {
		return triggerType;
	}

	/**
	 * 触发类型，可选值见该类型定义的常量TRIGGER_TYPE_*常量
	 * 
	 * @return
	 */
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public String getDayRunTime() {
		return dayRunTime;
	}

	/**
	 * 如果触发类型为TRIGGER_TYPE_DAY时，该字段指定每天的执行时间<br/>
	 * 格式为HH:mm:ss
	 * 
	 * @return
	 */
	public void setDayRunTime(String dayRunTime) {
		this.dayRunTime = dayRunTime;
	}

	public int[] getWeekRunDays() {
		return weekRunDays;
	}

	/**
	 * 如果触发类型为TRIGGER_TYPE_WEEK时，该字段指定执行的具体天数<br/>
	 * 格式为{1,3,5}
	 * 
	 * @return
	 */
	public void setWeekRunDays(int[] weekRunDays) {
		this.weekRunDays = weekRunDays;
	}

	public String getWeekRunTime() {
		return weekRunTime;
	}

	/**
	 * 如果触发类型为TRIGGER_TYPE_WEEK时，该字段指定执行的具体时间<br/>
	 * 格式为HH:mm:ss
	 * 
	 * @return
	 */
	public void setWeekRunTime(String weekRunTime) {
		this.weekRunTime = weekRunTime;
	}

	public int[] getMonthRunMonths() {
		return monthRunMonths;
	}

	/**
	 * 如果触发类型为TRIGGER_TYPE_MONTH时，该字段指定执行的具体月份<br/>
	 * 格式为{5,7,9}
	 * 
	 * @return
	 */
	public void setMonthRunMonths(int[] monthRunMonths) {
		this.monthRunMonths = monthRunMonths;
	}

	public String getMonthRunDay() {
		return monthRunDay;
	}

	/**
	 * 如果触发类型为TRIGGER_TYPE_MONTH时，该字段指定执行的具体日期<br/>
	 * 格式为：21，表示每个月的21号执行
	 * 
	 * @return
	 */
	public void setMonthRunDay(String monthRunDay) {
		this.monthRunDay = monthRunDay;
	}

	public String getMonthRunTime() {
		return monthRunTime;
	}

	/**
	 * 如果触发类型为TRIGGER_TYPE_MONTH时，该字段指定执行的具体时间<br/>
	 * 格式为：HH:mm:ss
	 * 
	 * @return
	 */
	public void setMonthRunTime(String monthRunTime) {
		this.monthRunTime = monthRunTime;
	}

	public String getCycleTime() {
		return cycleTime;
	}

	/**
	 * 如果触发类型为TRIGGER_TYPE_CYCLE时，该字段指定执行的起始时间
	 * 
	 * @return
	 */
	public void setCycleTime(String cycleTime) {
		this.cycleTime = cycleTime;
	}

	public int getCycleInterval() {
		return cycleInterval;
	}

	/**
	 * 如果触发类型为TRIGGER_TYPE_CYCLE时，该字段指定执行的时间间隔，小时为单位
	 * 
	 * @return
	 */
	public void setCycleInterval(int cycleInterval) {
		this.cycleInterval = cycleInterval;
	}

	public String getSelfCron() {
		return selfCron;
	}

	/**
	 * 如果触发类型为TRIGGER_TYPE_CRON时，该字段指定执行cron表达式
	 * 
	 * @return
	 */
	public void setSelfCron(String selfCron) {
		this.selfCron = selfCron;
	}
}
