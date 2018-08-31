package com.bdp.schedule.rest;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bdp.schedule.data.biz.ScheduleLogBiz;
import com.bdp.schedule.data.entity.ScheduleLog;
import com.bdp.schedule.dto.ScheduleInfo;
import com.bdp.schedule.dto.ScheduleLogDto;
import com.bdp.schedule.service.ScheduleService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	private ObjectMapper objMapper;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private ScheduleLogBiz scheduleLogBiz;

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
	 * 根据应用id,应用租户，调度ID，删除相应的调度
	 * 
	 * @param appId
	 *            应用ID
	 * @param appTenant
	 *            应用租户
	 * @param scheduleInfoId
	 *            调度ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/info/{appId}/{appTenant}/{scheduleInfoId}")
	public ResponseEntity<ScheduleInfo> get(@PathVariable("appId") String appId,
			@PathVariable("appTenant") String appTenant, @PathVariable("scheduleInfoId") String scheduleInfoId)
			throws Exception {
		ScheduleInfo scheduleInfo = scheduleService.get(appId, appTenant, scheduleInfoId);
		return ResponseEntity.ok().body(scheduleInfo);
	}

	/**
	 * 根据应用ID，应用租户，调度分类获取相应的调度列表
	 * 
	 * @param appId
	 *            应用ID
	 * @param appTenant
	 *            应用租户
	 * @param scheduleType
	 *            调度分类
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/list/{appId}/{appTenant}/{scheduleType}")
	public ResponseEntity<List<ScheduleInfo>> list(@PathVariable("appId") String appId,
			@PathVariable("appTenant") String appTenant, @PathVariable("scheduleInfoId") String scheduleType)
			throws Exception {
		List<ScheduleInfo> list = scheduleService.list(appId, appTenant, scheduleType);
		return ResponseEntity.ok().body(list);
	}

	/**
	 * 根据应用id,租户，调度ID，删除相应的调度
	 * 
	 * @param appId
	 *            应用ID
	 * @param appTenant
	 *            应用租户
	 * @param scheduleInfoId
	 *            调度ID
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/{appId}/{appTenant}/{scheduleInfoId}")
	public ResponseEntity<String> delete(@PathVariable("appId") String appId,
			@PathVariable("appTenant") String appTenant, @PathVariable("scheduleInfoId") String scheduleInfoId)
			throws Exception {
		try {
			scheduleService.delete(appId, appTenant, scheduleInfoId);
		} catch (Exception ex) {
			ex.printStackTrace();
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("调度删除失败：appId:" + appId + " appTenant:" + appTenant + " scheduleInfoId:" + scheduleInfoId);
		}
		return ResponseEntity.ok("SUCCESS");
	}

	/**
	 * 根据应用id,租户，调度ID，获取相应的日志信息
	 * 
	 * @param appId
	 *            应用ID
	 * @param appTenant
	 *            应用租户
	 * @param scheduleInfoId
	 *            调度ID
	 * @return 成功返回，则泛型中放的是List<ScheduleLogDto>，否则放的是异常信息字符串
	 * @throws Exception
	 */
	@GetMapping("/logs/{appId}/{appTenant}/{scheduleInfoId}")
	public ResponseEntity<?> getLogs(@PathVariable("appId") String appId, @PathVariable("appTenant") String appTenant,
			@PathVariable("scheduleInfoId") String scheduleInfoId) throws Exception {
		try {
			List<ScheduleLog> logs = scheduleLogBiz.getLogs(appId, appTenant, scheduleInfoId);
			String jsons = objMapper.writeValueAsString(logs);
			JavaType javaType = objMapper.getTypeFactory().constructParametricType(List.class, ScheduleLogDto.class);
			List<ScheduleLogDto> lgs = objMapper.readValue(jsons, javaType);
			return ResponseEntity.ok(lgs);
		} catch (Exception ex) {
			ex.printStackTrace();
			String errMsg = "日志获取失败，原因：" + ex.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errMsg);
		}
	}

	/**
	 * 根据 日志ID,写相应的日志信息
	 * 
	 * @param logId
	 *            日志ID
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/writeLog")
	public ResponseEntity<String> writeLog(@RequestParam("logId") String logId,
			@RequestParam(value = "status", defaultValue = "success") String status,
			@RequestParam(value = "logMsg", required = false) String logMsg) throws Exception {
		try {
			ScheduleLog log = scheduleLogBiz.readEntity(logId);
			if (log == null) {
				throw new Exception("调度日志【" + logId + "】信息不存在！");
			}
			log.setEndDate(new Date());
			log.setStatus(status);
			log.setLogMsg(logMsg);
			scheduleLogBiz.updateEntity(log);
			return ResponseEntity.ok("SUCCESS");
		} catch (Exception ex) {
			ex.printStackTrace();
			String errMsg = "日志写入失败，原因：" + ex.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errMsg);
		}
	}
}
