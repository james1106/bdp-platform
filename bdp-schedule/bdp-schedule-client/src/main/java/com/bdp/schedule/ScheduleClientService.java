package com.bdp.schedule;

import java.util.List;

import com.bdp.schedule.dto.GroupInfo;
import com.bdp.schedule.dto.ScheduleInfo;

/**
 * 调度客户端服务，由客户端实现
 * 
 * @author JACK
 *
 */
public interface ScheduleClientService {

	/**
	 * 列出所有的调度分组信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfo> listGroupInfos() throws Exception;

	/**
	 * 根据分组ID列出该分组下所有的调度信息
	 * 
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleInfo> listScheduleInfos(String groupId) throws Exception;

	/**
	 * 根据ID获取调度信息
	 * 
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public ScheduleInfo getScheduleInfoById(String scheduleId) throws Exception;
}
