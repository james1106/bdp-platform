package com.bdp.schedule.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bdp.schedule.dto.ScheduleInfo;

/**
 * 调度服务客户端<br/>
 * 在使用时需要加上如下启动类上如下注解： <br/>
 * @EnableFeignClients({"com.bdp.*.feign"})<br/>
 * 因为FeignClient的注册与其它spring bean的注册有区别
 * 
 * @author jack
 */
@FeignClient(name = "schedule/schedule")
public interface ScheduleFeignClient {

	/**
	 * 根据传入的Job信息，触发器信息创建调度任务
	 * 
	 * @param scheduleInfo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestBody ScheduleInfo scheduleInfo) throws Exception;

	/**
	 * 修改调度信息
	 * 
	 * @param scheduleInfo
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/update")
	public ResponseEntity<String> update(@RequestBody ScheduleInfo scheduleInfo) throws Exception;

	/**
	 * 根据应用id,应用角色，调度ID，删除相应的调度
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
			throws Exception;

	/**
	 * 根据应用ID，应用角色，调度分类获取相应的调度列表
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
			throws Exception;

	/**
	 * 根据应用id,角色，调度ID，删除相应的调度
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
			throws Exception;

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
			@PathVariable("scheduleInfoId") String scheduleInfoId) throws Exception;

	/**
	 * 根据 日志ID,写相应的日志信息
	 * 
	 * @param logId
	 *            日志ID
	 * @param status
	 *            success(成功),running(执行中),failure(失败)
	 * @param errorMsg
	 *            执行信息
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/writeLog")
	public ResponseEntity<String> writeLog(@RequestParam("logId") String logId,
			@RequestParam(value = "status", defaultValue = "success") String status,
			@RequestParam(value = "logMsg", required = false) String logMsg) throws Exception;
}
