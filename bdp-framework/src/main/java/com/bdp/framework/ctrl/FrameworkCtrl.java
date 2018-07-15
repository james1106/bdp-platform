package com.bdp.framework.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bdp.framework.data.biz.impl.BusisysBiz;
import com.bdp.framework.data.entity.Busisys;

@Controller
@RequestMapping
public class FrameworkCtrl {

	@Value("${bdp-platform.sys-code}")
	private String sysCode;

	@Autowired
	private BusisysBiz busisysBiz;

	@GetMapping
	public String index(ModelMap map) {
		Busisys busisys = busisysBiz.getBusisysByCode(sysCode);
		if (busisys != null) {
			map.put("busisys", busisys);
		}
		return "framework/index";
	}
}
