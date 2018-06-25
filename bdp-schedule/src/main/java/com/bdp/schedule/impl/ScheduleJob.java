package com.bdp.schedule.impl;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.bdp.schedule.client.JobClient;
import com.bdp.schedule.dto.ScheduleInfo;

public class ScheduleJob implements Job {

	// 使用Spring查询自动注入的，很有用
	@Autowired
	private List<JobClient> jobClients;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap datMap = context.getJobDetail().getJobDataMap();
		ScheduleInfo scheduleInfo = (ScheduleInfo) datMap.get(ScheduleInfo.SCHEDULEINFO_KEY_IN_JOBDETAIL);
		for (JobClient jobClient : jobClients) {
			if (jobClient.isSupport(scheduleInfo.getJobInfo())) {
				try {
					jobClient.doExecute(context, scheduleInfo.getJobInfo());
				} catch (Exception e) {
					throw new JobExecutionException(e);
				}
				break;
			}
		}
	}
}
