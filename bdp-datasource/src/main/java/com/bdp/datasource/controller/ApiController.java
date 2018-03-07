package com.bdp.datasource.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ApiController {

	@GetMapping("list")
	public List<String> list() {
		List<String> list = new ArrayList<String>();
		list.add("Jack");
		list.add("Rose");
		return list;
	}

	@GetMapping("/dataSource/{id}")
	public String getDataSourceById(@PathVariable("id") String id) {
		return "ds" + id;
	}
}
