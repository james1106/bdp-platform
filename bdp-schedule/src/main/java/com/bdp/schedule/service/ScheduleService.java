package com.bdp.schedule.service;

import java.util.List;

import com.bdp.schedule.dto.ScheduleInfo;

public interface ScheduleService {

	public void create(ScheduleInfo scheduleInfo) throws Exception;

	public void update(ScheduleInfo scheduleInfo) throws Exception;

	public ScheduleInfo get(String appId, String group, String name) throws Exception;

	public List<ScheduleInfo> list(String appId) throws Exception;

	public void delete(String appId, String group, String name) throws Exception;
}
