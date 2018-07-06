package com.bdp.schedule;

import java.util.List;

import com.bdp.schedule.dto.GroupInfo;
import com.bdp.schedule.dto.ScheduleInfo;

public interface ResourcesService {

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
}
