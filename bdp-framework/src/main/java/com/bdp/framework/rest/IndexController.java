package com.bdp.framework.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {

	@Value("${framework.busiSysCode}")
	private String busiSysId;

	@RequestMapping("/index")
	public ModelAndView list() {
		return new ModelAndView("framework/index");
	}
}
