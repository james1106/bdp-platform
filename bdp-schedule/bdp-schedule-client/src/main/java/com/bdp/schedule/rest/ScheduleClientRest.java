package com.bdp.schedule.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdp.schedule.ScheduleCallBack;
import com.bdp.schedule.ScheduleClientService;
import com.bdp.schedule.dto.GroupInfo;
import com.bdp.schedule.dto.ScheduleInfo;
import com.bdp.schedule.feign.ScheduleFeignClient;

@RestController
@RequestMapping("/scheduleClient")
public class ScheduleClientRest {

	@Autowired(required = false)
	private ScheduleClientService resourcesService;

	@Autowired
	private ScheduleFeignClient scheduleFeignClient;

	@Autowired
	private List<ScheduleCallBack> scheduleCallBacks;

	@GetMapping("/groupInfos")
	public ResponseEntity<List<GroupInfo>> listGroupInfos() throws Exception {
		if (resourcesService == null) {
			return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
		}
		return ResponseEntity.ok(resourcesService.listGroupInfos());
	}

	@GetMapping("/scheduleInfos/{groupId}")
	public ResponseEntity<List<ScheduleInfo>> listScheduleInfos(@PathVariable("groupId") String groupId)
			throws Exception {
		if (resourcesService == null) {
			return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
		}
		return ResponseEntity.ok(resourcesService.listScheduleInfos(groupId));
	}

	@GetMapping("/scheduleInfo/{scheduleId}")
	public ResponseEntity<ScheduleInfo> getScheduleInfoById(@PathVariable("scheduleId") String scheduleId)
			throws Exception {
		if (resourcesService == null) {
			return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
		}
		return ResponseEntity.ok(resourcesService.getScheduleInfoById(scheduleId));
	}

	@PostMapping("/execute")
	public ResponseEntity<String> execute(@RequestBody ScheduleInfo info, @RequestHeader("logId") String logId)
			throws Exception {
		String callBack = info.getJobInfo().getCallBack();
		for (ScheduleCallBack callBackImpl : scheduleCallBacks) {
			if (callBackImpl.isSupport(callBack)) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							callBackImpl.execute(info);
							scheduleFeignClient.writeLog(logId, "success", null);
						} catch (Exception e) {
							e.printStackTrace();
							try {
								scheduleFeignClient.writeLog(logId, "failure", null);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				});
				thread.start();
				return ResponseEntity.ok("【" + info.getName() + "】正在执行……");
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("【" + info.getName() + "】未找到对应的回调实现.");
	}

}
