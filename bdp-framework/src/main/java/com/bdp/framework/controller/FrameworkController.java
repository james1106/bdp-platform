package com.bdp.framework.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class FrameworkController {

	@Value("${bdp-platform.sys-code}")
	private String sysCode;

	@GetMapping
	public String index(ModelMap map) {
		return "framework/index";
	}
}
