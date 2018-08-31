package com.bdp.schedule.quartz.trigger;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bdp.schedule.dto.TriggerInfo;

/**
 * 按日执行调度生成器
 * 
 * @author JACK
 *
 */
@Component
public class DayTriggerCreator implements TriggerCreator {

	@Override
	public CronTrigger createTrigger(TriggerInfo triggerInfo) throws Exception {
		Logger logger = LoggerFactory.getLogger(getClass());
		String timeStr = triggerInfo.getDayRunTime();
		if (timeStr == null || "".equals(timeStr.trim())) {
			String info = "每天执行一次的调度计划：=>" + triggerInfo.getTriggerName() + " 的执行时间为空!";
			logger.error(info);
			throw new Exception(info);
		} else {
			String[] timeArr = timeStr.split(":");
			if (timeArr.length != 3) {
				logger.error("每天执行一次的调度计划：=>" + triggerInfo.getTriggerName() + " 的执行时间配置有误!");
			}
			if (timeArr[0].length() == 2 && timeArr[0].indexOf('0') == 0) {
				timeArr[0] = timeArr[0].substring(1);
			}
			if (timeArr[1].length() == 2 && timeArr[1].indexOf('0') == 0) {
				timeArr[1] = timeArr[1].substring(1);
			}
			if (timeArr[2].length() == 2 && timeArr[2].indexOf('0') == 0) {
				timeArr[2] = timeArr[2].substring(1);
			}
			int hour = Integer.parseInt(timeArr[0]);
			int minute = Integer.parseInt(timeArr[1]);
			int second = Integer.parseInt(timeArr[2]);

			String cronStr = second + " " + minute + " " + hour + " * * ?";
			logger.info("调度=>【" + triggerInfo.getTriggerName() + "】的调度规则字符串为：" + cronStr);
			return TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronStr)).build();
		}
	}

	@Override
	public boolean isSupport(TriggerInfo triggerInfo) {
		return StringUtils.equals(TriggerInfo.TRIGGER_TYPE_DAY, triggerInfo.getTriggerType());
	}

}
