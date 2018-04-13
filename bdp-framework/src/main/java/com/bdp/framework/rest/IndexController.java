package com.bdp.framework.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

	@Value("${bdp-platform.sys-code}")
	private String busiSysId;

	@RequestMapping("/index")
	public String index() {
		return "framework/index";
	}
	
	@RequestMapping("/list")
	public String list() {
		return "framework/user/list";
	}
}
