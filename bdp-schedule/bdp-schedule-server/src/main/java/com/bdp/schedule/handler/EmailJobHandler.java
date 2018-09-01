package com.bdp.schedule.handler;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.bdp.schedule.dto.JobInfo;
import com.bdp.schedule.dto.ScheduleInfo;

/**
 * 定时发送邮件的JOB
 * 
 * @author JACK
 *
 */
@Component
public class EmailJobHandler implements JobHandler {

	public static final String CLIENT_TYPE = "email";

	@Override
	public void doExecute(JobExecutionContext context, JobInfo jobInfo, ScheduleInfo scheduleInfo) throws JobExecutionException {

	}

	@Override
	public boolean isSupport(JobInfo jobInfo) {
		return StringUtils.equals(CLIENT_TYPE, jobInfo.getClientType());
	}
}
