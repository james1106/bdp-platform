package com.bdp.config.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/outApi")
public class OutServiceController {

	@GetMapping("/hello/{name}")
	public String sayHello(@PathVariable("name") String name) {
		return "hello " + name+" from config !";
	}
}
