package com.bdp.schedule.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bdp.schedule.data.biz.ScheduleLogBiz;
import com.bdp.schedule.data.entity.ScheduleLog;
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

	@Autowired
	private ScheduleLogBiz scheduleLogBiz;

	@Override
	public void doExecute(JobExecutionContext context, JobInfo jobInfo, ScheduleInfo scheduleInfo)
			throws JobExecutionException {
		ScheduleLog log = new ScheduleLog();
		log.setAppId(scheduleInfo.getAppId());
		log.setAppTenant(scheduleInfo.getAppTenant());
		log.setStartDate(new Date());
		log.setStatus(ScheduleLog.STATUS_RUNNING);
		scheduleLogBiz.createEntity(log);

		String url = jobInfo.getCallBack();
		if (!url.startsWith("http")) {
			url = "http://" + scheduleInfo.getAppId() + "/scheduleClient/execute";
		}
		Map<String, Object> params = jobInfo.getExtralParams();
		if (params == null) {
			params = new HashMap<>();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("logId", log.getId());
		// 请求租户信息
		headers.add("bdpProjectCode", scheduleInfo.getAppTenant());
		HttpEntity<ScheduleInfo> requestEntity = new HttpEntity<ScheduleInfo>(scheduleInfo, headers);

		ResponseEntity<String> result = null;
		if ("post".equalsIgnoreCase(jobInfo.getMethod())) {
			result = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, params);
		} else if ("get".equalsIgnoreCase(jobInfo.getMethod())) {
			result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, params);
		}
		if (result != null) {
			if (jobInfo.isSyncExecute()) {// 同步
				if (isSuccess(result)) {
					logger.info("作业【" + jobInfo.getJobName() + "】执行成功");
					log.setEndDate(new Date());
					log.setStatus(ScheduleLog.STATUS_SUCCESS);
				} else {
					logger.error("作业【" + jobInfo.getJobName() + "】执行失败,错误原因：" + result.getBody());
					log.setEndDate(new Date());
					log.setStatus(ScheduleLog.STATUS_FAILURE);
					log.setLogMsg(result.getBody());
				}
				scheduleLogBiz.updateEntity(log);
			} else {// 异步
				if (isSuccess(result)) {
					logger.info("调度=>【" + jobInfo.getJobName() + "】正在执行……");
				} else {// 即使是异步的，返回状态异常也认为执行失败
					logger.error("调度=>【" + jobInfo.getJobName() + "】执行失败,错误原因：" + result.getBody());
					log.setEndDate(new Date());
					log.setStatus(ScheduleLog.STATUS_FAILURE);
					log.setLogMsg(result.getBody());
				}
			}
		}
	}

	private boolean isSuccess(ResponseEntity<String> result) {
		// 兼容一下没有使用HttpStatus状态码的应用
		if (result.getStatusCode() == HttpStatus.OK
				&& (StringUtils.isEmpty(result.getBody()) || "SUCCESS".equalsIgnoreCase(result.getBody()))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isSupport(JobInfo jobInfo) {
		return StringUtils.equals(CLIENT_TYPE, jobInfo.getClientType());
	}
}
