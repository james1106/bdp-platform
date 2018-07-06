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
public interface ScheduleFeignService {

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
	 * @param appRole
	 *            应用角色
	 * @param scheduleInfoId
	 *            调度ID
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/{appId}/{appRole}/{scheduleInfoId}")
	public ResponseEntity<ScheduleInfo> get(@PathVariable("appId") String appId, @PathVariable("appRole") String appRole,
			@PathVariable("scheduleInfoId") String scheduleInfoId) throws Exception;

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
	public ResponseEntity<List<ScheduleInfo>> list(@PathVariable("appId") String appId, @PathVariable("appRole") String appRole,
			@PathVariable("scheduleInfoId") String scheduleType) throws Exception;

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
			@PathVariable("scheduleInfoId") String scheduleInfoId) throws Exception;
}
