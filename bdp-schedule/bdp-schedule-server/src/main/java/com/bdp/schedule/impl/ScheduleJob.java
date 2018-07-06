package com.bdp.schedule.impl;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bdp.schedule.client.JobClient;
import com.bdp.schedule.dto.ScheduleInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ScheduleJob implements Job {

	Logger logger = LoggerFactory.getLogger(getClass());

	// 使用Spring查询自动注入的，很有用
	@Autowired
	private List<JobClient> jobClients;

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap datMap = context.getJobDetail().getJobDataMap();
		String json = (String) datMap.get(ScheduleInfo.SCHEDULEINFO_KEY);
		ScheduleInfo scheduleInfo = null;
		try {
			scheduleInfo = objectMapper.readValue(json, ScheduleInfo.class);
		} catch (Exception ex) {
			logger.error("ScheduleInfo json反序列化错误,JSON为:" + json);
			throw new JobExecutionException(ex);
		}
		for (JobClient jobClient : jobClients) {
			if (jobClient.isSupport(scheduleInfo.getJobInfo())) {
				try {
					jobClient.doExecute(context, scheduleInfo.getJobInfo(), scheduleInfo);
				} catch (Exception e) {
					throw new JobExecutionException(e);
				}
				break;
			}
		}
	}
}
