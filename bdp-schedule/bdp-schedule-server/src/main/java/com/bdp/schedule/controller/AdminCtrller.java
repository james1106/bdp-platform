package com.bdp.schedule.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdp.schedule.data.entity.ScheduleLog;
import com.bdp.schedule.dto.ScheduleInfo;

@RestController
@RequestMapping("/admin")
public class AdminCtrller {

	@GetMapping("/list")
	public List<ScheduleInfo> list() {
		return null;
	}
	
	@DeleteMapping("/delete")
	public List<ScheduleInfo> delete(String key) {
		return null;
	}
	
	@GetMapping("/logs")
	public List<ScheduleLog> logs(String key) {
		return null;
	}
}
