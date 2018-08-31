package com.bdp.schedule.quartz.listener;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bdp.schedule.dto.ScheduleInfo;
import com.bdp.schedule.utils.DateUtil;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class GlobalTriggerListener implements TriggerListener {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ListenerHelper helper;

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		ScheduleInfo scheduleInfo = helper.getScheduleInfo(context);
		logger.info("调度=>【" + scheduleInfo.getName() + "】开始执行，本次执行时间为:" + DateUtil.format(new Date()));
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		ScheduleInfo scheduleInfo = helper.getScheduleInfo(context);
		logger.info("调度=>【" + scheduleInfo.getName() + "】执行完成，下次执行时间为:" + DateUtil.format(trigger.getNextFireTime()));
	}
}
