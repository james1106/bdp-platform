package com.bdp.schedule.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdp.schedule.ResourcesService;
import com.bdp.schedule.dto.GroupInfo;
import com.bdp.schedule.dto.ScheduleInfo;

@RestController
@RequestMapping("/scheduleRes")
public class ResuorcesServiceRest {

	@Autowired(required = false)
	private ResourcesService resourcesService;

	@GetMapping("/groupInfos")
	public List<GroupInfo> listGroupInfos() throws Exception {
		if (resourcesService == null) {
			return new ArrayList<GroupInfo>();
		}
		return resourcesService.listGroupInfos();
	}

	@GetMapping("/scheduleInfos/{groupId}")
	public List<ScheduleInfo> listScheduleInfos(@PathVariable("groupId") String groupId) throws Exception {
		if (resourcesService == null) {
			return new ArrayList<ScheduleInfo>();
		}
		return resourcesService.listScheduleInfos(groupId);
	}

}
