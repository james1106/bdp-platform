package com.bdp.schedule.service;

import java.util.List;

import com.bdp.schedule.dto.ScheduleInfo;

public interface ScheduleService {

	/**
	 * 根据给定信息创建调度任务
	 * 
	 * @param scheduleInfo
	 *            调度信息
	 * @throws Exception
	 */
	public void create(ScheduleInfo scheduleInfo) throws Exception;

	/**
	 * 根据给定信息，更新调度任务
	 * 
	 * @param scheduleInfo
	 *            调度任务
	 * @throws Exception
	 */
	public void update(ScheduleInfo scheduleInfo) throws Exception;

	/**
	 * 获取调度信息
	 * 
	 * @param appId
	 *            应用ID
	 * @param appRole
	 *            应用角色
	 * @param scheduleInfoId
	 *            调度ID
	 * @return
	 * @throws Exception
	 */
	public ScheduleInfo get(String appId, String appRole, String scheduleInfoId) throws Exception;

	/**
	 * 查询调度信息
	 * 
	 * @param appId
	 *            应用ID
	 * @param appRole
	 *            应用角色
	 * @param scheduleType
	 *            调度类型，为空则查询当前应用及当前角色下所有的调度信息
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleInfo> list(String appId, String appRole, String scheduleType) throws Exception;

	/**
	 * 删除调度信息
	 * 
	 * @param appId
	 *            应用ID
	 * @param appRole
	 *            应用角色
	 * @param scheduleInfoId
	 *            调度ID
	 * @throws Exception
	 */
	public void delete(String appId, String appRole, String scheduleInfoId) throws Exception;
}
