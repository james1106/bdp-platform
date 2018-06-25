package com.bdp.schedule.client;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.bdp.schedule.dto.JobInfo;

/**
 * 这个用于Feign做为客户端远程调用的JOB
 * 
 * @author JACK
 *
 */
@Component
public class FeignJobClient implements JobClient {

	public static final String CLIENT_TYPE = "feign";

	@Override
	public void doExecute(JobExecutionContext context, JobInfo jobInfo) throws JobExecutionException {

	}

	@Override
	public boolean isSupport(JobInfo jobInfo) {
		return StringUtils.equals(CLIENT_TYPE, jobInfo.getClientType());
	}
}
