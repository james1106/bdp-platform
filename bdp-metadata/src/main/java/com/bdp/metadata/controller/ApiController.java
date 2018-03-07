package com.bdp.metadata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdp.metadata.outerservices.DataSourceService;

@RestController
@RequestMapping("/")
public class ApiController {

	@Autowired
	private DataSourceService dataSourceService;

	@GetMapping("/dsMetadata/{dsId}")
	public String dsMetadata(@PathVariable("dsId") String dsId) {
		String dsName = dataSourceService.getDataSourceById(dsId);
		return "Return DataSource [" + dsName + "] MetaData.";
	}
}
