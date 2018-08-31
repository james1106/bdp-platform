package com.bdp.schedule.quartz.listener;

import java.util.Date;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bdp.schedule.dto.ScheduleInfo;
import com.bdp.schedule.dto.TriggerInfo;
import com.bdp.schedule.utils.DateUtil;

@Component
public class GlobalJobListener implements JobListener {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	Scheduler scheduler;

	@Autowired
	private ListenerHelper helper;

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		ScheduleInfo scheduleInfo = helper.getScheduleInfo(context);
		logger.info("调度【" + scheduleInfo.getName() + "】准备调度，时间为:" + DateUtil.format(new Date()));
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		logger.info("execute GlobalJobListener.jobExecutionVetoed(JobExecutionContext context)");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		ScheduleInfo scheduleInfo = helper.getScheduleInfo(context);
		logger.info("调度【" + scheduleInfo.getName() + "】执行完毕，时间为:" + DateUtil.format(new Date()));
		JobDetail jobDetail = context.getJobDetail();
		// 记录当前调度已经执行了多少次
		String sc = helper.getJobData(jobDetail, "schedule_count");
		if (sc == null) {
			// 属性只能存字符串
			helper.setJobData(jobDetail, "schedule_count", "1");
		} else {
			int scNum = Integer.parseInt(sc);
			// 按次数据过期
			TriggerInfo triggerInfo = scheduleInfo.getTriggerInfo();
			if (TriggerInfo.EXPIRE_TYPE_TIMES.equals(triggerInfo.getExpireType())) {
				if ((scNum + 1) == triggerInfo.getExpireTimes()) {// 已经执行了指定的次数,从调度器中移除
					try {
						scheduler.deleteJob(context.getJobDetail().getKey());
						logger.info("调度【" + scheduleInfo.getName() + "】，按次过期，已经执行了【" + (scNum + 1) + "】次，执行完结，移除调度器。");
						return;
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			} else if (TriggerInfo.EXPIRE_TYPE_DATE.equals(triggerInfo.getExpireType())) {
				try {
					JobKey jobKey = context.getJobDetail().getKey();
					List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
					for (Trigger trigger : triggers) {
						if (trigger.getNextFireTime().after(triggerInfo.getExpireDate())) {
							scheduler.deleteJob(context.getJobDetail().getKey());
							logger.info("调度【" + scheduleInfo.getName() + "】，按时间过期，过期时间为【"
									+ DateUtil.format(triggerInfo.getExpireDate()) + "】，下次执行时间为：【"
									+ DateUtil.format(trigger.getNextFireTime()) + "】，已经过期，执行完结，移除调度器。");
							return;
						}
						continue;
					}
				} catch (Exception e) {
					e.printStackTrace();
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			logger.info("调度【" + scheduleInfo.getName() + "】，已经执行了【" + (scNum + 1) + "】次。");
			// 属性只能存字符串
			helper.setJobData(jobDetail, "schedule_count", (scNum + 1) + "");
		}
	}

}
