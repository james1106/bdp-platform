package com.bdp.schedule.client;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdp.schedule.dto.JobInfo;
import com.bdp.schedule.dto.ScheduleInfo;

/**
 * 根据isSupport来确定是否为JOB的具体执行类
 * 
 * @author JACK
 *
 */
public interface JobClient {
	/**
	 * 当前JOB是否支持可以处理传入的jobInfo.<br/>
	 * 
	 * @return
	 */
	public boolean isSupport(JobInfo jobInfo);

	/**
	 * 执行JOB
	 * 
	 * @param context
	 * @param jobInfo
	 * @throws JobExecutionException
	 */
	public void doExecute(JobExecutionContext context, JobInfo jobInfo, ScheduleInfo scheduleInfo) throws Exception;
}
