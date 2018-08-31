package com.bdp.schedule.data.biz;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.framework.data.biz.impl.BaseBizImpl;
import com.bdp.schedule.data.entity.ScheduleLog;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScheduleLogBiz extends BaseBizImpl<ScheduleLog> {

	public List<ScheduleLog> getLogs(String appId, String appTenant, String scheduleInfoId) {
		String jpql = "from ScheduleLog l where l.appId=? and l.appTenant=? and l.scheduleInfoId=?";
		return getEntitiesByJpql(jpql);
	}

}
