package com.bdp.schedule.quartz.listener;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bdp.schedule.dto.ScheduleInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ListenerHelper {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	Scheduler scheduler;

	@Autowired
	private ObjectMapper objectMapper;

	public ScheduleInfo getScheduleInfo(JobExecutionContext context) {
		JobDetail jobDetail = context.getJobDetail();
		return getScheduleInfo(jobDetail);
	}

	@SuppressWarnings("unchecked")
	public <T> T getJobData(JobDetail jobDetail, String key) {
		JobDataMap map = jobDetail.getJobDataMap();
		if (map.containsKey(key)) {
			return (T) map.get(key);
		}
		return null;
	}

	public void setJobData(JobDetail jobDetail, String key, Object value) {
		JobDataMap map = jobDetail.getJobDataMap();
		map.put(key, value);
	}

	public ScheduleInfo getScheduleInfo(JobDetail jobDetail) {
		if (jobDetail == null) {
			return null;
		}
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

	public ScheduleInfo getScheduleInfo(JobKey jobKey) throws Exception {
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		return getScheduleInfo(jobDetail);
	}
}
