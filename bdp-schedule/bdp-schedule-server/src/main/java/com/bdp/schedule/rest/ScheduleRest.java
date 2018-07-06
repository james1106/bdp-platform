package com.bdp.schedule.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdp.schedule.dto.ScheduleInfo;
import com.bdp.schedule.service.ScheduleService;

/**
 * 对外提供服务，供第三方应用调用
 * 
 * @author JACK
 *
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleRest {

	@Autowired
	private ScheduleService scheduleService;

	/**
	 * 根据传入的Job信息，触发器信息创建调度任务
	 * 
	 * @param scheduleInfo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestBody ScheduleInfo scheduleInfo) throws Exception {
		scheduleService.create(scheduleInfo);
		return ResponseEntity.ok("SUCCESS");
	}

	/**
	 * 修改调度信息
	 * 
	 * @param scheduleInfo
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/update")
	public ResponseEntity<String> update(@RequestBody ScheduleInfo scheduleInfo) throws Exception {
		scheduleService.update(scheduleInfo);
		return ResponseEntity.ok("SUCCESS");
	}

	/**
	 * 根据应用id,应用角色，调度ID，删除相应的调度
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
	@GetMapping("/{appId}/{appRole}/{scheduleInfoId}")
	public ResponseEntity<ScheduleInfo> get(@PathVariable("appId") String appId,
			@PathVariable("appRole") String appRole, @PathVariable("scheduleInfoId") String scheduleInfoId)
			throws Exception {
		ScheduleInfo scheduleInfo = scheduleService.get(appId, appRole, scheduleInfoId);
		return ResponseEntity.ok().body(scheduleInfo);
	}

	/**
	 * 根据应用ID，应用角色，调度分类获取相应的调度列表
	 * 
	 * @param appId
	 *            应用ID
	 * @param appRole
	 *            应用角色
	 * @param scheduleType
	 *            调度分类
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/list/{appId}/{appRole}/{scheduleType}")
	public ResponseEntity<List<ScheduleInfo>> list(@PathVariable("appId") String appId,
			@PathVariable("appRole") String appRole, @PathVariable("scheduleInfoId") String scheduleType)
			throws Exception {
		scheduleService.list(appId, appRole, scheduleType);
		return ResponseEntity.ok().body(new ArrayList<ScheduleInfo>());
	}

	/**
	 * 根据应用id,角色，调度ID，删除相应的调度
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
	@DeleteMapping("/{appId}/{appRole}/{scheduleInfoId}")
	public ResponseEntity<String> delete(@PathVariable("appId") String appId, @PathVariable("appRole") String appRole,
			@PathVariable("scheduleInfoId") String scheduleInfoId) throws Exception {
		try {
			scheduleService.delete(appId, appRole, scheduleInfoId);
		} catch (Exception ex) {
			ex.printStackTrace();
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("调度删除失败：appId:" + appId + " appRole:" + appRole + " scheduleInfoId:" + scheduleInfoId);
		}
		return ResponseEntity.ok("SUCCESS");
	}
}
