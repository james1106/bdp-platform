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
 * 自定义执行调度生成器
 * 
 * @author JACK
 *
 */
@Component
public class CronTriggerCreator implements TriggerCreator {

	@Override
	public CronTrigger createTrigger(TriggerInfo triggerInfo) throws Exception {
		Logger logger = LoggerFactory.getLogger(getClass());
		if (StringUtils.isEmpty(triggerInfo.getSelfCron())) {
			String info = "自定义执行调度【" + triggerInfo.getTriggerName() + "】的cron字符串未指定";
			logger.error(info);
			throw new Exception(info);
		}
		logger.info("计划=>【" + triggerInfo.getTriggerName() + "】的调度规则字符串为：" + triggerInfo.getSelfCron());
		CronTrigger trigger = TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule(triggerInfo.getSelfCron())).build();
		return trigger;
	}

	@Override
	public boolean isSupport(TriggerInfo triggerInfo) {
		return StringUtils.equals(TriggerInfo.TRIGGER_TYPE_CRON, triggerInfo.getTriggerType());
	}
}
