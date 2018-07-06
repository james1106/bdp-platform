package com.bdp.schedule.client;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bdp.schedule.dto.JobInfo;
import com.bdp.schedule.dto.ScheduleInfo;

/**
 * 用于Http远程调用的JOB
 * 
 * @author JACK
 *
 */
@Component
public class HttpJobClient implements JobClient {

	public static final String CLIENT_TYPE = "http";
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	RestTemplate restTemplate;

	@Override
	public void doExecute(JobExecutionContext context, JobInfo jobInfo, ScheduleInfo scheduleInfo)
			throws JobExecutionException {
		String url = jobInfo.getCallBackPath();
		Map<String, Object> params = jobInfo.getExtralParams();
		// 调用restTemplate()拿到的是是同一个Bean
		ResponseEntity<String> result = null;
		if ("post".equalsIgnoreCase(jobInfo.getMethod())) {
			result = restTemplate.postForEntity(url, params, String.class);
		} else if ("get".equalsIgnoreCase(jobInfo.getMethod())) {
			result = restTemplate.getForEntity(url, String.class, params);
		}
		if (result != null) {
			if (result.getStatusCode() == HttpStatus.OK) {
				logger.info("作业【" + jobInfo.getJobName() + "】执行成功");
			} else {
				logger.error("作业【" + jobInfo.getJobName() + "】执行失败,错误原因：" + result.getBody());
			}
		}
	}

	@Override
	public boolean isSupport(JobInfo jobInfo) {
		return StringUtils.equals(CLIENT_TYPE, jobInfo.getClientType());
	}

}
