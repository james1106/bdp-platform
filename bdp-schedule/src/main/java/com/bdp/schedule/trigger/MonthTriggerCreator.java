package com.bdp.schedule.trigger;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.springframework.stereotype.Component;

import com.bdp.schedule.dto.TriggerInfo;

@Component
public class MonthTriggerCreator implements TriggerCreator {

	@Override
	public CronTrigger createTrigger(TriggerInfo triggerInfo) throws Exception {
		return null;
	}

	@Override
	public boolean isSupport(TriggerInfo triggerInfo) {
		return StringUtils.equals(TriggerInfo.TRIGGER_TYPE_MONTH, triggerInfo.getTriggerType());
	}
}
