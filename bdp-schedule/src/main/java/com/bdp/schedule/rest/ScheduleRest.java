package com.bdp.schedule.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdp.schedule.dto.ScheduleInfo;
import com.bdp.schedule.dto.TriggerInfo;
import com.bdp.schedule.service.ScheduleService;

@RestController
@RequestMapping("/schedule")
public class ScheduleRest {

	@Autowired
	private ScheduleService scheduleService;

	/**
	 * 根据传入的Job信息，触发器信息创建调度任务
	 * 
	 * @param jobInfo
	 *            job信息
	 * @param triggerInfo
	 *            触发器信息
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/create")
	public ResponseEntity<String> create(ScheduleInfo scheduleInfo) throws Exception {
		scheduleService.create(scheduleInfo);
		return ResponseEntity.ok("SUCCESS");
	}

	/**
	 * 根据应用id,分组，名称及触发器信息修改相应的调度
	 * 
	 * @param appId
	 *            应用ID
	 * @param group
	 *            分组
	 * @param name
	 *            名称
	 * @param triggerInfo
	 *            触发器信息
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/update/{appId}/{group}/{name}")
	public ResponseEntity<String> update(@PathVariable("appId") String appId, @PathVariable("group") String group,
			@PathVariable("name") String name, TriggerInfo triggerInfo) throws Exception {
		return ResponseEntity.ok("SUCCESS");
	}

	/**
	 * 根据应用id,分组，名称获取相应的调度
	 * 
	 * @param appId
	 *            应用ID
	 * @param group
	 *            分组
	 * @param name
	 *            名称
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/{appId}/{group}/{name}")
	public ResponseEntity<String> get(@PathVariable("appId") String appId, @PathVariable("group") String group,
			@PathVariable("name") String name) throws Exception {

		return ResponseEntity.ok("SUCCESS");
	}

	/**
	 * 列中某个应用下被调度服务调度的信息
	 * 
	 * @param appId
	 *            应用ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/{appId}")
	public ResponseEntity<String> list(@PathVariable("appId") String appId) throws Exception {

		return ResponseEntity.ok("SUCCESS");
	}

	/**
	 * 根据应用id,分组，名称删除相应的调度
	 * 
	 * @param appId
	 *            应用ID
	 * @param group
	 *            分组
	 * @param name
	 *            名称
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/{appId}/{group}/{name}")
	public ResponseEntity<String> delete(@PathVariable("appId") String appId, @PathVariable("group") String group,
			@PathVariable("name") String name) throws Exception {
		scheduleService.delete(appId, group, name);
		return ResponseEntity.ok("SUCCESS");
	}
}
