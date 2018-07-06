package com.bdp.schedule.listener;

import org.quartz.JobDataMap;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GlobalSchedulerListener extends SchedulerListenerSupport {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	Scheduler scheduler;

	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void jobAdded(JobDetail jobDetail) {
		logger.info("execute GlobalSchedulerListener.jobAdded(JobDetail jobDetail)");
		ScheduleInfo scheduleInfo = getScheduleInfo(jobDetail);
		if (scheduleInfo != null) {
			logger.info("作业【" + scheduleInfo.getName() + "】加入调度器");
		}
	}

	@Override
	public void jobPaused(JobKey jobKey) {
		logger.info("execute GlobalSchedulerListener.jobPaused(JobKey jobKey)");
		try {
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			if (jobDetail != null) {
				ScheduleInfo scheduleInfo = getScheduleInfo(jobDetail);
				if (scheduleInfo != null) {
					logger.info("作业【" + scheduleInfo.getName() + "】暂停");
				}
			}
		} catch (SchedulerException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void schedulerError(String msg, SchedulerException cause) {
		logger.info("execute GlobalSchedulerListener.schedulerError(String msg, SchedulerException cause)");
		logger.error("调度执行异常：" + msg);
	}

	private ScheduleInfo getScheduleInfo(JobDetail jobDetail) {
		logger.info("execute GlobalSchedulerListener.getScheduleInfo(JobDetail jobDetail)");
		JobDataMap datMap = jobDetail.getJobDataMap();
		String json = (String) datMap.get(ScheduleInfo.SCHEDULEINFO_KEY);
		ScheduleInfo scheduleInfo = null;
		try {
			scheduleInfo = objectMapper.readValue(json, ScheduleInfo.class);
			return scheduleInfo;
		} catch (Exception ex) {
			logger.error("ScheduleInfo json反序列化错误,JSON为:" + json);
			throw new RuntimeException(ex);
		}
	}
}
