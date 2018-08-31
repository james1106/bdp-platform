package com.bdp.schedule.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ListenerManager;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.MutableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdp.schedule.dto.ScheduleInfo;
import com.bdp.schedule.dto.TriggerInfo;
import com.bdp.schedule.quartz.ScheduleJob;
import com.bdp.schedule.quartz.listener.GlobalJobListener;
import com.bdp.schedule.quartz.listener.GlobalSchedulerListener;
import com.bdp.schedule.quartz.listener.GlobalTriggerListener;
import com.bdp.schedule.quartz.trigger.TriggerCreator;
import com.bdp.schedule.service.ScheduleService;
import com.bdp.schedule.utils.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private List<TriggerCreator> triggerCreators;

	// Spring MVC中内置的，可直接使用
	@Autowired
	private ObjectMapper oMapper;

	/**
	 * 直接注入，这是由SchedulerFactoryBean自动创建的。
	 * 因为SchedulerFactoryBean实现了spring的FactoryBean接口
	 */
	@Autowired
	private Scheduler scheduler;

	@Autowired
	private GlobalJobListener jobListener;

	@Autowired
	private GlobalTriggerListener triggerListener;

	@Autowired
	private GlobalSchedulerListener schedulerListener;

	@PostConstruct
	public void init() throws Exception {
		ListenerManager listenerMgr = scheduler.getListenerManager();
		listenerMgr.addJobListener(jobListener);
		listenerMgr.addTriggerListener(triggerListener);
		listenerMgr.addSchedulerListener(schedulerListener);
	}

	@Override
	public void create(ScheduleInfo scheduleInfo) throws Exception {
		TriggerInfo triggerInfo = scheduleInfo.getTriggerInfo();
		CronTrigger trigger = null;
		for (TriggerCreator creator : triggerCreators) {
			if (creator.isSupport(triggerInfo)) {
				trigger = creator.createTrigger(triggerInfo);
				TriggerKey triggerKey = createTriggerKey(scheduleInfo.getId(), scheduleInfo.getAppId(),
						scheduleInfo.getAppTenant());
				((MutableTrigger) trigger).setKey(triggerKey);
				break;
			}
		}
		if (trigger == null) {
			throw new Exception("未找到对应的触发器生成类,triggerInfo:" + oMapper.writeValueAsString(triggerInfo));
		}
		JobKey jobKey = createJobKey(scheduleInfo.getId(), scheduleInfo.getAppId(), scheduleInfo.getAppTenant());
		JobDetail job = JobBuilder.newJob(ScheduleJob.class).withIdentity(jobKey).build();
		job.getJobDataMap().put(ScheduleInfo.SCHEDULEINFO_KEY, oMapper.writeValueAsString(scheduleInfo));
		scheduler.scheduleJob(job, trigger);
		logger.info("调度【" + scheduleInfo.getName() + "】加入到调度器，下次执行的时间为：" + DateUtil.format(trigger.getNextFireTime()));
	}

	@Override
	public void update(ScheduleInfo scheduleInfo) throws Exception {
		delete(scheduleInfo.getAppId(), scheduleInfo.getAppTenant(), scheduleInfo.getId());
		create(scheduleInfo);
	}

	@Override
	public ScheduleInfo get(String appId, String appTenant, String scheduleInfoId) throws Exception {
		JobDetail jobDetail = scheduler.getJobDetail(createJobKey(scheduleInfoId, appId, appTenant));
		String json = (String) jobDetail.getJobDataMap().get(ScheduleInfo.SCHEDULEINFO_KEY);
		ScheduleInfo scheduleInfo = oMapper.readValue(json, ScheduleInfo.class);
		return scheduleInfo;
	}

	@Override
	public List<ScheduleInfo> list(String appId, String appTenant, String scheduleType) throws Exception {
		if (StringUtils.isEmpty(appTenant)) {
			appTenant = "";
		}
		String group = "@" + appId + "_on_" + appTenant;
		GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals(group);
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<ScheduleInfo> list = new ArrayList<>();
		for (JobKey jobKey : jobKeys) {
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			String json = (String) jobDetail.getJobDataMap().get(ScheduleInfo.SCHEDULEINFO_KEY);
			ScheduleInfo info = oMapper.readValue(json, ScheduleInfo.class);
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			TriggerKey triggerKey = null;
			for (Trigger trigger : triggers) {
				triggerKey = trigger.getKey();
				continue;
			}
			String status = null;
			if (triggerKey != null) {
				TriggerState state = scheduler.getTriggerState(triggerKey);
				if (state == TriggerState.NORMAL) {
					status = "normal";
				} else if (state == TriggerState.COMPLETE) {
					status = "complete";
				} else if (state == TriggerState.PAUSED) {
					status = "paused";
				} else if (state == TriggerState.BLOCKED) {
					status = "block";
				} else if (state == TriggerState.ERROR) {
					status = "error";
				} else if (state == TriggerState.NONE) {
					status = "none";
				}
				info.setState(status);
			}
			if (StringUtils.isNotEmpty(scheduleType) && scheduleType.equals(info.getScheduleType())) {
				list.add(info);
			} else if (StringUtils.isEmpty(scheduleType)) {
				list.add(info);
			}
		}
		return list;
	}

	@Override
	public void delete(String appId, String appTenant, String scheduleInfoId) throws Exception {
		JobKey jobKey = createJobKey(scheduleInfoId, appId, appTenant);
		if (scheduler.checkExists(jobKey)) {
			scheduler.interrupt(jobKey);
			scheduler.deleteJob(jobKey);
		}
	}

	private JobKey createJobKey(String id, String appId, String appTenant) {
		if (StringUtils.isEmpty(appTenant)) {
			appTenant = "";
		}
		return new JobKey(id, "@" + appId + "_on_" + appTenant);
	}

	private TriggerKey createTriggerKey(String id, String appId, String appTenant) {
		if (StringUtils.isEmpty(appTenant)) {
			appTenant = "";
		}
		return new TriggerKey(id, "@" + appId + "_on_" + appTenant);
	}

	@Override
	public void pause(String appId, String appTenant, String scheduleInfoId) throws Exception {
		scheduler.pauseJob(createJobKey(scheduleInfoId, appId, appTenant));
	}

	@Override
	public void resume(String appId, String appTenant, String scheduleInfoId) throws Exception {
		scheduler.resumeJob(createJobKey(scheduleInfoId, appId, appTenant));
	}
}
