package com.bdp.schedule.quartz;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bdp.schedule.dto.ScheduleInfo;
import com.bdp.schedule.handler.JobHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 添加两个注解，保证同一时间只有一个实例执行，<br/>
 * 且执行完成后会把JobDataMap中修改的数据持久化
 * 
 * @author JACK
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ScheduleJob implements Job {

	Logger logger = LoggerFactory.getLogger(getClass());

	// 使用Spring查询自动注入的，很有用
	@Autowired
	private List<JobHandler> jobHandlers;

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
		for (JobHandler jobHandler : jobHandlers) {
			if (jobHandler.isSupport(scheduleInfo.getJobInfo())) {
				try {
					jobHandler.doExecute(context, scheduleInfo.getJobInfo(), scheduleInfo);
				} catch (Exception e) {
					throw new JobExecutionException(e);
				}
				break;
			}
		}
	}
}
