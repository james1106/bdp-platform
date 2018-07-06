package com.bdp.schedule.trigger;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bdp.schedule.dto.TriggerInfo;

/**
 * 按月执行调度生成器
 * 
 * @author JACK
 *
 */
@Component
public class MonthTriggerCreator implements TriggerCreator {

	@Override
	public CronTrigger createTrigger(TriggerInfo triggerInfo) throws Exception {
		Logger logger = LoggerFactory.getLogger(getClass());
		String months = triggerInfo.getMonthRunMonths();
		String[] time = triggerInfo.getMonthRunTime().split(":");
		if (time.length != 3) {
			String info = triggerInfo.getTriggerName() + "月执行规则执行时间配置有误:" + triggerInfo.getMonthRunTime();
			logger.error(info);
			throw new Exception(info);
		}
		String hour = time[0];
		String minute = time[1];
		String second = time[2];
		String day = triggerInfo.getMonthRunDay();
		String cronStr = second + " " + minute + " " + hour + " " + day + " " + months.replace("#", ",") + " ?";
		logger.info("计划=>【" + triggerInfo.getTriggerName() + "】的调度规则字符串为：" + cronStr);
		CronTrigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronStr))
				.build();
		return trigger;

	}

	@Override
	public boolean isSupport(TriggerInfo triggerInfo) {
		return StringUtils.equals(TriggerInfo.TRIGGER_TYPE_MONTH, triggerInfo.getTriggerType());
	}
}
