package com.bdp.datasource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ServiceInfoController {

	@GetMapping("/info")
	public String info() {
		return "Wellcome BDP DataSource Center.";
	}
}
