package com.bdp.schedule.quartz.listener;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.listeners.SchedulerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bdp.schedule.dto.ScheduleInfo;

@Component
public class GlobalSchedulerListener extends SchedulerListenerSupport {

	@Autowired
	Scheduler scheduler;

	@Autowired
	private ListenerHelper helper;

	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void jobAdded(JobDetail jobDetail) {
		ScheduleInfo scheduleInfo = helper.getScheduleInfo(jobDetail);
		if (scheduleInfo != null) {
			logger.info("调度=>【" + scheduleInfo.getName() + "】加入调度器");
		}
	}

	@Override
	public void jobPaused(JobKey jobKey) {
		try {
			ScheduleInfo scheduleInfo = helper.getScheduleInfo(jobKey);
			if (scheduleInfo != null) {
				logger.info("调度=>【" + scheduleInfo.getName() + "】暂停");
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void jobResumed(JobKey jobKey) {
		try {
			ScheduleInfo scheduleInfo = helper.getScheduleInfo(jobKey);
			if (scheduleInfo != null) {
				logger.info("调度=>【" + scheduleInfo.getName() + "】重启");
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void schedulerError(String msg, SchedulerException cause) {
		logger.error("调度执行异常：" + msg);
	}
}
